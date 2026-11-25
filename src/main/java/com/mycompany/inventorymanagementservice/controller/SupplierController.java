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
import com.mycompany.inventorymanagementservice.dto.SupplierDTO;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.service.CategoryService;
import com.mycompany.inventorymanagementservice.service.SupplierService;
import com.mycompany.inventorymanagementservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
	
	private final SupplierService supplierService;
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> addSupplier(@RequestBody @Valid SupplierDTO supplierDTO){
		
		return ResponseEntity.ok(supplierService.addSupplier(supplierDTO));
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<Response> getAllSuppliers(){
		
		return ResponseEntity.ok(supplierService.getAllSuppliers());
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response> getSupplierById(@PathVariable Long id){
		
		return ResponseEntity.ok(supplierService.getSupplierById(id));
		
	}
	
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> updateSupplier(@PathVariable Long id, @RequestBody @Valid SupplierDTO supplierDTO ){
		
		return ResponseEntity.ok(supplierService.updateSupplier(id, supplierDTO));
		
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteSupplier(@PathVariable Long id ){
		
		return ResponseEntity.ok(supplierService.deleteSupplier(id));
		
	}

	
	

}
