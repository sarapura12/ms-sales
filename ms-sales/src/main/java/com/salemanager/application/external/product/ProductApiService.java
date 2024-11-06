package com.salemanager.application.external.product;

import com.salemanager.application.external.product.models.ProductDto;
import org.springframework.web.client.RestTemplate;

public class ProductApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8082/api";

    public ProductApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductDto getProductById(Long productId) {
        String uri = baseUrl + "/products/" + productId;
        return restTemplate.getForObject(uri, ProductDto.class);
    }
}
