package com.salemanager.application.service.impl;

import com.salemanager.application.dto.PlaceOrderDto;
import com.salemanager.application.exception.InvalidOperationException;
import com.salemanager.application.exception.ResourceNotFoundException;
import com.salemanager.application.external.client.ClientApiService;
import com.salemanager.application.external.client.models.ClientDto;
import com.salemanager.application.external.product.ProductApiService;
import com.salemanager.application.external.product.models.ProductDto;
import com.salemanager.application.external.product.models.ProductStatus;
import com.salemanager.application.model.entity.Sale;
import com.salemanager.application.model.enums.PaymentMethod;
import com.salemanager.application.repository.SaleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleServiceImplTest {

    @Mock
    private ClientApiService clientApiService;

    @Mock
    private ProductApiService productApiService;

    @Mock
    private SaleRepository saleRepository;

    @InjectMocks
    private SaleServiceImpl saleService;

    private PlaceOrderDto placeOrderDto;
    private Sale sale;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        placeOrderDto = new PlaceOrderDto();
        placeOrderDto.setClientId(1L);
        placeOrderDto.setProductId(1L);
        placeOrderDto.setQuantity(2L);
        placeOrderDto.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        sale = new Sale();
        sale.setClientId(1L);
        sale.setProductId(1L);
        sale.setProductPrice(100.0);
        sale.setDateTime(LocalDateTime.now());
        sale.setPaymentMethod(PaymentMethod.CREDIT_CARD);

        productDto = new ProductDto();
        productDto.setStatus(ProductStatus.AVAILABLE);
        productDto.setStock(10);
        productDto.setPrice(100.0);
    }

    @AfterEach
    void tearDown() {
        reset(clientApiService, productApiService, saleRepository);
        placeOrderDto = null;
        sale = null;

    }

    @Test
    void generateSale_Success() {
        when(clientApiService.getClientById(anyLong())).thenReturn(new ClientDto());
        when(productApiService.getProductById(anyLong())).thenReturn(productDto);
        when(saleRepository.save(any(Sale.class))).thenReturn(sale);

        Sale createdSale = saleService.generateSale(placeOrderDto);

        assertNotNull(createdSale);
        assertEquals(1L, createdSale.getClientId());
        assertEquals(1L, createdSale.getProductId());
        verify(saleRepository, times(1)).save(any(Sale.class));
    }

    @Test
    void generateSale_ClientNotFound() {
        when(clientApiService.getClientById(anyLong())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            saleService.generateSale(placeOrderDto);
        });

        assertEquals("Client", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());
        verify(saleRepository, times(0)).save(any(Sale.class));
    }

    @Test
    void generateSale_ProductNotFound() {
        when(clientApiService.getClientById(anyLong())).thenReturn(new ClientDto());
        when(productApiService.getProductById(anyLong())).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            saleService.generateSale(placeOrderDto);
        });

        assertEquals("Product", exception.getResourceName());
        assertEquals("id", exception.getFieldName());
        assertEquals(1L, exception.getFieldValue());
        verify(saleRepository, times(0)).save(any(Sale.class));
    }

    @Test
    void generateSale_ProductNotAvailable() {
        productDto.setStatus(ProductStatus.NOT_AVAILABLE);
        when(clientApiService.getClientById(anyLong())).thenReturn(new ClientDto());
        when(productApiService.getProductById(anyLong())).thenReturn(productDto);

        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            saleService.generateSale(placeOrderDto);
        });

        assertEquals("Product Status, ", exception.getResourceName());
        assertEquals("Product is inactive", exception.getMessage());
        verify(saleRepository, times(0)).save(any(Sale.class));
    }

    @Test
    void generateSale_NotEnoughStock() {
        productDto.setStock(1);
        when(clientApiService.getClientById(anyLong())).thenReturn(new ClientDto());
        when(productApiService.getProductById(anyLong())).thenReturn(productDto);

        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            saleService.generateSale(placeOrderDto);
        });


        assertEquals("Not enough stock to complete the order", exception.getMessage());
        verify(saleRepository, times(0)).save(any(Sale.class));
    }
}