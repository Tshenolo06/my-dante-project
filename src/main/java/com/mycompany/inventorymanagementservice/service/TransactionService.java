package com.mycompany.inventorymanagementservice.service;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.TransactionRequest;
import com.mycompany.inventorymanagementservice.enums.TransactionStatus;

public interface TransactionService {
	
	Response restockInventory(TransactionRequest transactionRequest);
	
	Response sell(TransactionRequest transactionRequest);
	
	Response returnToSupplier(TransactionRequest transactionRequest);
	
	Response getAllTransactions(int page, int size, String searchText);
	
	Response getAllTransactionsByMonthAndYear(int month, int year);
	
	Response getTransactionsById(Long id);
	
	Response updateTransactionStatus(Long transactionId, TransactionStatus transactionStatus);
	
	Response deleteCategory(Long id);
	
	

}
