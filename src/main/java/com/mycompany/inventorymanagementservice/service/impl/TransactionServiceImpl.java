package com.mycompany.inventorymanagementservice.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.TransactionDTO;
import com.mycompany.inventorymanagementservice.dto.TransactionRequest;
import com.mycompany.inventorymanagementservice.entity.Product;
import com.mycompany.inventorymanagementservice.entity.Supplier;
import com.mycompany.inventorymanagementservice.entity.Transaction;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.enums.TransactionStatus;
import com.mycompany.inventorymanagementservice.enums.TransactionType;
import com.mycompany.inventorymanagementservice.exception.NameValueRequiredException;
import com.mycompany.inventorymanagementservice.exception.NotFoundException;
import com.mycompany.inventorymanagementservice.repository.ProductRepository;
import com.mycompany.inventorymanagementservice.repository.SupplierRepository;
import com.mycompany.inventorymanagementservice.repository.TransactionRepository;
import com.mycompany.inventorymanagementservice.repository.UserRepository;
import com.mycompany.inventorymanagementservice.security.JwtUtils;
import com.mycompany.inventorymanagementservice.service.TransactionService;
import com.mycompany.inventorymanagementservice.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;




@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{
	
	public final TransactionRepository transactionRepository;
	
	public final ModelMapper modelMapper;
	
	public final SupplierRepository supplierRepository;
	
	public final UserService userService;
	
	public final ProductRepository productRepository;

	@Override
	public Response restockInventory(TransactionRequest transactionRequest) {

		Long productId = transactionRequest.getProductId();
		Long supplierId = transactionRequest.getSupplierId();
		Integer quantity = transactionRequest.getQuantity();
		
		if(supplierId == null) throw new NameValueRequiredException("Supplier Id is required");
		
		Product product = productRepository.findById(productId)
				.orElseThrow(()-> new NotFoundException("Product not found"));
		
		Supplier supplier = supplierRepository.findById(supplierId)
				.orElseThrow(()-> new NotFoundException("Supplier not found"));
			
		User user = userService.getCurrentLoggedInUser();	
		
		product.setStockQuantity(product.getStockQuantity() + quantity);
		
		productRepository.save(product);
		
		
		Transaction transaction = Transaction.builder()
				.transactionType(TransactionType.PURCHASE)
				.status(TransactionStatus.COMPLETED)
				.product(product)
				.user(user)
				.supplier(supplier)
				.totalProduct(quantity)
				.totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
				.description(transactionRequest.getDescription())
				.build();
			
		transactionRepository.save(transaction);
		
		return Response.builder()
				.status(200)
				.message("Transaction was made successfull")
				.build();
			
		

		
	}

	@Override
	public Response sell(TransactionRequest transactionRequest) {
		
		Long productId = transactionRequest.getProductId();
		
		Integer quantity = transactionRequest.getQuantity();
		
		
		
		Product product = productRepository.findById(productId)
				.orElseThrow(()-> new NotFoundException("Product not found"));
		
		
			
		User user = userService.getCurrentLoggedInUser();	
		
		product.setStockQuantity(product.getStockQuantity() - quantity);
		
		productRepository.save(product);
		
		
		Transaction transaction = Transaction.builder()
				.transactionType(TransactionType.SALE)
				.status(TransactionStatus.COMPLETED)
				.product(product)
				.user(user)
				.totalProduct(quantity)
				.totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
				.description(transactionRequest.getDescription())
				.build();
			
		transactionRepository.save(transaction);
		
		return Response.builder()
				.status(200)
				.message("Transaction was sold successfull")
				.build();
		
		
		
	}

	@Override
	public Response returnToSupplier(TransactionRequest transactionRequest) {
		
		Long productId = transactionRequest.getProductId();
		Long supplierId = transactionRequest.getSupplierId();
		Integer quantity = transactionRequest.getQuantity();
		
		if(supplierId == null) throw new NameValueRequiredException("Supplier Id is required");
		
		Product product = productRepository.findById(productId)
				.orElseThrow(()-> new NotFoundException("Product not found"));
		
		Supplier supplier = supplierRepository.findById(supplierId)
				.orElseThrow(()-> new NotFoundException("Supplier not found"));
			
		User user = userService.getCurrentLoggedInUser();	
		
		product.setStockQuantity(product.getStockQuantity() - quantity);
		
		productRepository.save(product);
		
		
		Transaction transaction = Transaction.builder()
				.transactionType(TransactionType.RETURN_TO_SUPPLIER)
				.status(TransactionStatus.PROCESSING)
				.product(product)
				.user(user)
				.supplier(supplier)
				.totalProduct(quantity)
				.totalPrice(BigDecimal.ZERO)
				.description(transactionRequest.getDescription())
				.build();
			
		transactionRepository.save(transaction);
		
		return Response.builder()
				.status(200)
				.message("Transaction returned successfully initialised")
				.build();
		
		
		
		
	}

	@Override
	public Response getAllTransactions(int page, int size, String searchText) {
		
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<Transaction> transactionPage = transactionRepository.searchTransactions(searchText, pageable); 
		List<TransactionDTO> transactionDTO = modelMapper
				.map(transactionPage.getContent(), new TypeToken<List<TransactionDTO>>() {}.getType());
		
		transactionDTO.forEach(transactionDTOItem->{
			
			transactionDTOItem.setUser(null);
			transactionDTOItem.setProduct(null);
			transactionDTOItem.setSupplier(null);
			
			
			
			
			
		});
		
		return Response.builder()
				.status(200)
				.message("Success")
				.transactions(transactionDTO)
				.build();
		
		
	}
	
	@Override
	public Response getTransactionsById(Long id) {
		
			Transaction transaction = transactionRepository.findById(id)
					.orElseThrow(()-> new NotFoundException("Transaction not found"));
			
			TransactionDTO transactionDTO = modelMapper.map(transaction, TransactionDTO.class);
			transactionDTO.getUser().setTransactions(null);
			
			return Response.builder()
					.status(200)
					.message("Success")
					.transaction(transactionDTO)
					.build();
		
		
	}

	@Override
	public Response getAllTransactionsByMonthAndYear(int month, int year) {
		
		List<Transaction> transactions = transactionRepository.findAllByMonthAndYear(month, year);
		
		List<TransactionDTO> transactionDTO = modelMapper
				.map(transactions, new TypeToken<List<TransactionDTO>>() {}.getType());
		
		transactionDTO.forEach(transactionDTOItem->{
			
			transactionDTOItem.setUser(null);
			transactionDTOItem.setProduct(null);
			transactionDTOItem.setSupplier(null);
			
			
			
			
			
		});
		
		return Response.builder()
				.status(200)
				.message("Success")
				.transactions(transactionDTO)
				.build();
		
		
		
	}

	

	@Override
	public Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus) {
		
		Transaction existingTransaction = transactionRepository.findById(transactionId)
				.orElseThrow(()-> new NotFoundException("Transaction not found"));
		
		existingTransaction.setStatus(transactionStatus);
		existingTransaction.setUpdatedAt(LocalDateTime.now());
		
		transactionRepository.save(existingTransaction);
		
		return Response.builder()
				.status(200)
				.message("Transaction status saved successfull")
				.build();
		
		
	}

	@Override
	public Response deleteCategory(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
