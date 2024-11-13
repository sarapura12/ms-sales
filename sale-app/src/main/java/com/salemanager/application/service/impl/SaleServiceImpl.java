package com.salemanager.application.service.impl;

import com.salemanager.application.dto.PlaceOrderDto;
import com.salemanager.application.exception.ResourceNotFoundException;
import com.salemanager.application.external.client.ICustomerFeignClient;
import com.salemanager.application.external.product.IProductFeignClient;
import com.salemanager.application.model.entity.Sale;
import com.salemanager.application.repository.SaleRepository;
import com.salemanager.application.service.interfaces.IEmailService;
import com.salemanager.application.service.interfaces.ISaleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaleServiceImpl implements ISaleService {
    private final ICustomerFeignClient customerFeignClient;
    private final IProductFeignClient productFeignClient;
    private final SaleRepository saleRepository;
    private final IEmailService emailService;

    public SaleServiceImpl(ICustomerFeignClient customerFeignClient, IProductFeignClient productFeignClient, SaleRepository saleRepository, IEmailService emailService) {
        this.customerFeignClient = customerFeignClient;
        this.productFeignClient = productFeignClient;
        this.saleRepository = saleRepository;
        this.emailService = emailService;
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
        Map<String, Object> variables = new HashMap<>();
        variables.put("client", client);
        variables.put("sale", newSale);
        variables.put("product", product);

        try {
            emailService.sendMail(client.getEmail(), "Order Confirmation", variables);
        }catch (Exception e){
            e.printStackTrace();
        }
        return newSale;
    }
}
