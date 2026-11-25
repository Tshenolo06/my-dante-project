package com.mycompany.inventorymanagementservice.service;

import org.springframework.web.multipart.MultipartFile;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.ProductDTO;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.SupplierDTO;

public interface ProductService {
	
	Response saveProduct(ProductDTO productDTO, MultipartFile imageFile);
	
	Response updateProduct(ProductDTO productDTO, MultipartFile imageFile);
	
	Response getAllProducts();
	
	Response getProductById(Long id);
	
	Response deleteProduct(Long id);
	
	

}
