package com.salemanager.application.external.product;

import com.salemanager.application.external.product.models.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("ms-products")
public interface IProductFeignClient {
    @RequestMapping(method = RequestMethod.GET, value = "/api/products/{productId}", consumes = "application/json")
    ProductDto getProductById(@PathVariable Long productId);
    @RequestMapping(method = RequestMethod.PUT, value = "/api/products/{productId}/discount?quantity={quantity}", consumes = "application/json")
    ProductDto requestDiscount(@PathVariable Long productId, @PathVariable Integer quantity);
}
