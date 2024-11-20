package com.salemanager.application.service.impl;

import com.salemanager.application.dto.PlaceOrderDto;
import com.salemanager.application.exception.ResourceNotFoundException;
import com.salemanager.application.external.client.ICustomerFeignClient;
import com.salemanager.application.external.client.models.ClientDto;
import com.salemanager.application.external.product.IProductFeignClient;
import com.salemanager.application.external.product.models.ProductDto;
import com.salemanager.application.model.entity.Sale;
import com.salemanager.application.repository.SaleRepository;
import com.salemanager.application.service.interfaces.IEmailService;
import com.salemanager.application.service.interfaces.ISaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(SaleServiceImpl.class);

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

        sendEmail(client, newSale, product);
        return newSale;
    }

    private void sendEmail(ClientDto client, Sale newSale, ProductDto product) {
        try {
            Map<String, Object> dataEmail = new HashMap<>();
            dataEmail.put("client", client);
            dataEmail.put("sale", newSale);
            dataEmail.put("product", product);

            emailService.sendMail(client.getEmail(), "Order Confirmation", dataEmail);
        } catch (Exception e) {
            logger.error("Error sending email: {}", e.getMessage());
        }
    }
}
