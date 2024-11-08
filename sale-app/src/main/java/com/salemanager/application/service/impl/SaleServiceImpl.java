package com.salemanager.application.service.impl;

import com.salemanager.application.dto.PlaceOrderDto;
import com.salemanager.application.exception.ResourceNotFoundException;
import com.salemanager.application.external.client.ClientApiService;
import com.salemanager.application.external.product.ProductApiService;
import com.salemanager.application.model.entity.Sale;
import com.salemanager.application.repository.SaleRepository;
import com.salemanager.application.service.interfaces.ISaleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SaleServiceImpl implements ISaleService {
    private final ClientApiService clientApiService;
    private final ProductApiService productApiService;
    private final SaleRepository saleRepository;

    public SaleServiceImpl(ClientApiService clientApiService, ProductApiService productApiService, SaleRepository saleRepository) {
        this.clientApiService = clientApiService;
        this.productApiService = productApiService;
        this.saleRepository = saleRepository;
    }

    @Override
    public Sale generateSale(PlaceOrderDto placeOrderDto) {
        var client = clientApiService.getClientById(placeOrderDto.getClientId());
        if (client == null) {
            throw new ResourceNotFoundException("Client", "id", placeOrderDto.getClientId());
        }

        var product = productApiService.getProductById(placeOrderDto.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", placeOrderDto.getProductId());
        }

        var result = productApiService.requestDiscount(placeOrderDto.getProductId(), 1);

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
