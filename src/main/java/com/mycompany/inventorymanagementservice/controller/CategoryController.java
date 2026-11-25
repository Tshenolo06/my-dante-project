package com.mycompany.inventorymanagementservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.LoginRequest;
import com.mycompany.inventorymanagementservice.dto.RegisterRequest;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.service.CategoryService;
import com.mycompany.inventorymanagementservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
	
	private final CategoryService categoryService;
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> createCategories(@RequestBody @Valid CategoryDTO categoryDTO){
		
		return ResponseEntity.ok(categoryService.createCategory(categoryDTO));
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<Response> getAllCategories(){
		
		return ResponseEntity.ok(categoryService.getAllCategories());
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response> getCategoriesById(@PathVariable Long id){
		
		return ResponseEntity.ok(categoryService.getCategoryById(id));
		
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDTO categoryDTO ){
		
		return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
		
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteCategory(@PathVariable Long id ){
		
		return ResponseEntity.ok(categoryService.deleteCategory(id));
		
	}

	
	

}
