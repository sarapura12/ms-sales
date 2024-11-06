package com.salemanager.application.repository;

import com.salemanager.application.model.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}
