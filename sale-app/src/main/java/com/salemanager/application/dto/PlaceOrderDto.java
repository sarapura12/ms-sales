package com.salemanager.application.dto;

import com.salemanager.application.model.enums.PaymentMethod;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceOrderDto {
    private Long clientId;
    private Long productId;
    private Integer quantity = 1;
    private PaymentMethod paymentMethod;
}
