package com.salemanager.application.external.product;

import com.salemanager.application.external.product.models.ProductDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8081/api";

    public ProductApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProductDto getProductById(Long productId) {
        String uri = baseUrl + "/products/" + productId;
        return restTemplate.getForObject(uri, ProductDto.class);
    }
}
