package com.salemanager.application.model.entity;

import com.salemanager.application.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTime;
    private Long clientId;
    private Long productId;
    private double productPrice;
    @Enumerated(EnumType.ORDINAL)
    private PaymentMethod paymentMethod;
}
