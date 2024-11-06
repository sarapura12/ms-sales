package com.salemanager.application.controller;

import com.salemanager.application.external.client.ClientApiService;
import com.salemanager.application.external.product.ProductApiService;
import com.salemanager.application.external.product.models.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sales")

public class SaleController {
    private final ClientApiService clientApiService;
    private final ProductApiService productApiService;

    public SaleController(ClientApiService clientApiService, ProductApiService productApiService) {
        this.clientApiService = clientApiService;
        this.productApiService = productApiService;
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getSale(@PathVariable Long productId) {
        return ResponseEntity.ok(productApiService.getProductById(productId));
    }
}
