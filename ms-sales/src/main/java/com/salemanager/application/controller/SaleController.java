package com.salemanager.application.controller;

import com.salemanager.application.dto.PlaceOrderDto;
import com.salemanager.application.model.entity.Sale;
import com.salemanager.application.service.impl.SaleServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")

public class SaleController {
    private final SaleServiceImpl saleService;


    public SaleController(SaleServiceImpl saleService) {
        this.saleService = saleService;

    }

    @PostMapping
    public ResponseEntity<Sale> getSale(@RequestBody PlaceOrderDto placeOrderDto) {
        return ResponseEntity.ok(saleService.generateSale(placeOrderDto));
    }
}
