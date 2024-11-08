package com.salemanager.application.external.product;

import com.salemanager.application.external.product.models.ProductDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductApiService {
    private final RestTemplate restTemplate;
    private final String baseUrl = "http://localhost:8081/api";

    public ProductApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private  <T> T sendPutRequest(String url, T body, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<T> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, responseType);
        return responseEntity.getBody();
    }

    public ProductDto getProductById(Long productId) {
        String uri = baseUrl + "/products/" + productId;
        return restTemplate.getForObject(uri, ProductDto.class);
    }

    public ProductDto requestDiscount(Long productId, Integer quantity) {
        String uri = baseUrl + "/products/" + productId + "/discount?quantity=" + quantity;
        return sendPutRequest(uri, null, ProductDto.class);
    }
}
