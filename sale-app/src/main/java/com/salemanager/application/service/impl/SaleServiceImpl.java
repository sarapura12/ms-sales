package com.salemanager.application.service.impl;

import com.salemanager.application.dto.PlaceOrderDto;
import com.salemanager.application.exception.ResourceNotFoundException;
import com.salemanager.application.external.client.ICustomerFeignClient;
import com.salemanager.application.external.product.IProductFeignClient;
import com.salemanager.application.model.entity.Sale;
import com.salemanager.application.repository.SaleRepository;
import com.salemanager.application.service.interfaces.ISaleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaleServiceImpl implements ISaleService {
    private final ICustomerFeignClient customerFeignClient;
    private final IProductFeignClient productFeignClient;
    private final SaleRepository saleRepository;

    public SaleServiceImpl(ICustomerFeignClient customerFeignClient, IProductFeignClient productFeignClient, SaleRepository saleRepository) {
        this.customerFeignClient = customerFeignClient;
        this.productFeignClient = productFeignClient;
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale generateSale(PlaceOrderDto placeOrderDto) {
        var client = customerFeignClient.getClientById(placeOrderDto.getClientId());
        if (client == null) {
            throw new ResourceNotFoundException("Client", "id", placeOrderDto.getClientId());
        }

        var product = productFeignClient.getProductById(placeOrderDto.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", placeOrderDto.getProductId());
        }

        var result = productFeignClient.requestDiscount(placeOrderDto.getProductId(), placeOrderDto.getQuantity());

        var newSale = new Sale();
        newSale.setClientId(placeOrderDto.getClientId());
        newSale.setProductId(result.getId());
        newSale.setProductPrice(product.getPrice());
        newSale.setDateTime(LocalDateTime.now());
        newSale.setPaymentMethod(placeOrderDto.getPaymentMethod());

        saleRepository.save(newSale);

        return newSale;
    }
}
