package com.mycompany.inventorymanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.inventorymanagementservice.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

}
