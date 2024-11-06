package com.salemanager.application.external.product.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String imageName;
    private String code;
    private ProductStatus status;
    private Long supplierId;
    private Double price;
    private Integer stock;
}
