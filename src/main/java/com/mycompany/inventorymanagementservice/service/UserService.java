package com.mycompany.inventorymanagementservice.service;

import com.mycompany.inventorymanagementservice.dto.LoginRequest;
import com.mycompany.inventorymanagementservice.dto.RegisterRequest;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.User;

public interface UserService {
	
	Response registerUser(RegisterRequest registerRequest);
	
	Response loginUser(LoginRequest loginRequest);
	
	Response getAllUsers();
	
	User getCurrentLoggedInUser();
	
	Response updateUser(Long id, UserDTO userDTO);
	
	Response deleteUser(Long id);
	
	Response getUserTransactions(Long id);

}
