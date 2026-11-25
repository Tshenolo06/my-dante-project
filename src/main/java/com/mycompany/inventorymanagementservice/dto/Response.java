package com.mycompany.inventorymanagementservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mycompany.inventorymanagementservice.enums.UserRole;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
	
	private int status;
	
	private String message;
	
	private String token;
	
	private UserRole role;
	
	private String expirationTime;
	
	private Integer totalPages;
	
	private Long totalElement;
	
	private UserDTO user;
	
	private List<UserDTO> users;
	
	private SupplierDTO supplier;
	
	private List<SupplierDTO> suppliers;
	
	private CategoryDTO category;
	
	private List<CategoryDTO> categories;
	
	private ProductDTO product;
	
	private List<ProductDTO> products;
	
	private TransactionDTO transaction;
	
	private List<TransactionDTO> transactions;
	
	private final LocalDateTime timeStamp = LocalDateTime.now();

}
