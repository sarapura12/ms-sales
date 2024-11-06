package com.salemanager.application.service.interfaces;

import com.salemanager.application.dto.PlaceOrderDto;
import com.salemanager.application.model.entity.Sale;

public interface ISaleService {
    Sale generateSale(PlaceOrderDto placeOrderDto);
}
