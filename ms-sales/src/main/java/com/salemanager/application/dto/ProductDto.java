package com.salemanager.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private String id;
    private String name;
    private String code;
    private String description;
    private double price;
    private Integer stock;
}
