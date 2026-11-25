package com.mycompany.inventorymanagementservice.service;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.SupplierDTO;

public interface SupplierService {
	
	Response addSupplier(SupplierDTO supplierDTO);
	
	Response getAllSuppliers();
	
	Response getSupplierById(Long id);
	
	Response updateSupplier(Long id, SupplierDTO supplierDTO);
	
	Response deleteSupplier(Long id);
	
	

}
