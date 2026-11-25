package com.mycompany.inventorymanagementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.inventorymanagementservice.entity.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier,Long>{

}
