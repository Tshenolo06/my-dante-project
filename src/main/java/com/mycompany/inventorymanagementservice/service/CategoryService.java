package com.mycompany.inventorymanagementservice.service;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.Response;

public interface CategoryService {
	
	Response createCategory(CategoryDTO categoryDTO);
	
	Response getAllCategories();
	
	Response getCategoryById(Long id);
	
	Response updateCategory(Long id, CategoryDTO categoryDTO);
	
	Response deleteCategory(Long id);
	
	

}
