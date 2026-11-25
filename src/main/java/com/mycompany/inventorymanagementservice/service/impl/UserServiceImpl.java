package com.mycompany.inventorymanagementservice.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycompany.inventorymanagementservice.dto.LoginRequest;
import com.mycompany.inventorymanagementservice.dto.RegisterRequest;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.enums.UserRole;
import com.mycompany.inventorymanagementservice.exception.InvalidCredentialsException;
import com.mycompany.inventorymanagementservice.exception.NotFoundException;
import com.mycompany.inventorymanagementservice.repository.UserRepository;
import com.mycompany.inventorymanagementservice.security.JwtUtils;
import com.mycompany.inventorymanagementservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	private final ModelMapper modelMapper;
	
	private final JwtUtils jwtUtils;
	

	@Override
	public Response registerUser(RegisterRequest registerRequest) {
		// TODO Auto-generated method stub
		
		UserRole role = UserRole.MANAGER;
		
		if(registerRequest.getRole() !=null) {
			
			role = registerRequest.getRole();
			
		}
		
		User userTosave = User.builder()
				.name(registerRequest.getName())
				.email(registerRequest.getEmail())
				.password(passwordEncoder.encode(registerRequest.getPassword()))
				.phoneNumber(registerRequest.getPhoneNumber())
				.role(role)
				.build();
		
		userRepository.save(userTosave);
		
		return Response.builder()
				.status(200)
				.message("user created successfully")
				.build();
	}

	@Override
	public Response loginUser(LoginRequest loginRequest) {
		
		User user = userRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(()-> new NotFoundException("email not found"));
				
				if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
					
					throw new InvalidCredentialsException("password do not match");
						
				}
				
				String token = jwtUtils.generateToken(user.getEmail());
				
				return Response.builder()
						.status(200)
						.message("user logged in successfully")
						.role(user.getRole())
						.token(token)
						.expirationTime("6 month")
						.build();
				
	}

	@Override
	public Response getAllUsers() {

		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		users.forEach(user -> user.setTransactions(null));
		List<UserDTO> userDTO = modelMapper.map(users, new TypeToken<List<UserDTO>>() {}.getType());
		
		return Response.builder()
				.status(200)
				.message("success")
				.users(userDTO)
				.build();
		
	}

	@Override
	public User getCurrentLoggedInUser() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		String email = authentication.getName();
		
		User user = userRepository.findByEmail(email)
				.orElseThrow(()-> new NotFoundException("user not found"));
		
		user.setTransactions(null);
		
		return user;
		
	}

	@Override
	public Response updateUser(Long id, UserDTO userDTO) {
		
		User existingUser = userRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("user not found"));
		
		if(userDTO.getEmail() !=null )existingUser.setEmail(userDTO.getEmail());
		if(userDTO.getName() !=null )existingUser.setName(userDTO.getName());
		if(userDTO.getPhoneNumber() !=null )existingUser.setPhoneNumber(userDTO.getPhoneNumber());
		if(userDTO.getRole() !=null )existingUser.setRole(userDTO.getRole());
		
		
		
		if(userDTO.getPassword() !=null  && !userDTO.getPassword().isEmpty()){
			
			existingUser.setPhoneNumber(passwordEncoder.encode(userDTO.getPassword()));
		}
		
		userRepository.save(existingUser);
		
		return Response.builder()
				.status(200)
				.message("user successfully updated")
				.build();
		
	}

	@Override
	public Response deleteUser(Long id) {
		
		User existingUser = userRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("user not found"));
		
		userRepository.deleteById(id);
		
		return Response.builder()
				.status(200)
				.message("user successfully deleted")
				.build();
		
		
	}

	@Override
	public Response getUserTransactions(Long id) {
		
		
		User user = userRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("user not found"));
		
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);

		userDTO.getTransactions().forEach(transactionDTO ->{
			transactionDTO.setUser(null);
			transactionDTO.setSupplier(null);
		});
		
		return Response.builder()
				.status(200)
				.message("success")
				.user(userDTO)
				.build();
		
	}

}
