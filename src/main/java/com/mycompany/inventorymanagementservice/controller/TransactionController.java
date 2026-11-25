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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.LoginRequest;
import com.mycompany.inventorymanagementservice.dto.RegisterRequest;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.TransactionDTO;
import com.mycompany.inventorymanagementservice.dto.TransactionRequest;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.enums.TransactionStatus;
import com.mycompany.inventorymanagementservice.service.CategoryService;
import com.mycompany.inventorymanagementservice.service.TransactionService;
import com.mycompany.inventorymanagementservice.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
	
	private final TransactionService transactionService;
	
	@PostMapping("/purchase")
	public ResponseEntity<Response> restockInventory(@RequestBody @Valid TransactionRequest transactionRequest){
		
		return ResponseEntity.ok(transactionService.restockInventory(transactionRequest));
		
	}
	
	@PostMapping("/sell")
	public ResponseEntity<Response> sell(@RequestBody @Valid TransactionRequest transactionRequest){
		
		return ResponseEntity.ok(transactionService.sell(transactionRequest));
		
	}
	
	@PostMapping("/return")
	public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest){
		
		return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
		
	}


	
	@GetMapping("/all")
	public ResponseEntity<Response> getAllTransactions(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "1000") int size,
			@RequestParam(required = false) String searchText){
		
		return ResponseEntity.ok(transactionService.getAllTransactions(page, size, searchText));
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Response> getTransactionsById(@PathVariable Long id){
		
		return ResponseEntity.ok(transactionService.getTransactionsById(id));
		
	}
	
	@GetMapping("/by-month-year")
	public ResponseEntity<Response> getAllTransactionsByMonthAndYear(
			@RequestParam int month,
			@RequestParam int year){
		
		return ResponseEntity.ok(transactionService.getAllTransactionsByMonthAndYear(month, year));
		
	}
	
	@PutMapping("/update/{transactionId}")
	public ResponseEntity<Response> updateTransactionStatus(@PathVariable Long transactionId,
											@RequestBody @Valid TransactionStatus transactionStatus ){
		
		return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId, transactionStatus));
		
	}
	
	
	
	

}
