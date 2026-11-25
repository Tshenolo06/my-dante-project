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

import com.mycompany.inventorymanagementservice.dto.LoginRequest;
import com.mycompany.inventorymanagementservice.dto.RegisterRequest;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@GetMapping("/all")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> getAllUsers( ){
		
		return ResponseEntity.ok(userService.getAllUsers());
		
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<Response> updateUser(@PathVariable Long id,@RequestBody @Valid UserDTO userDTO ){
		
		return ResponseEntity.ok(userService.updateUser(id, userDTO));
		
	}
	
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Response> deleteUser(@PathVariable Long id ){
		
		return ResponseEntity.ok(userService.deleteUser(id));
		
	}
	
	@GetMapping("/transaction/{id}")
	public ResponseEntity<Response> getUserAndTransactions(@PathVariable Long id ){
		
		return ResponseEntity.ok(userService.getUserTransactions(id));
		
	}
	
	@GetMapping("/current")
	public ResponseEntity<User> getCurrentUser(){
		
		return ResponseEntity.ok(userService.getCurrentLoggedInUser());
		
	}


	
	

}
