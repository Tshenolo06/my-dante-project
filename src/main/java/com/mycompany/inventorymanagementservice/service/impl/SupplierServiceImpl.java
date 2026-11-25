package com.mycompany.inventorymanagementservice.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.SupplierDTO;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.Category;
import com.mycompany.inventorymanagementservice.entity.Supplier;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.exception.NotFoundException;
import com.mycompany.inventorymanagementservice.repository.CategoryRepository;
import com.mycompany.inventorymanagementservice.repository.SupplierRepository;
import com.mycompany.inventorymanagementservice.repository.UserRepository;
import com.mycompany.inventorymanagementservice.security.JwtUtils;
import com.mycompany.inventorymanagementservice.service.CategoryService;
import com.mycompany.inventorymanagementservice.service.SupplierService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService{
	
	private final SupplierRepository supplierRepository;
	
	private final ModelMapper modelMapper;

	@Override
	public Response addSupplier(SupplierDTO supplierDTO) {
		
		Supplier supplierToSave = modelMapper.map(supplierDTO, Supplier.class);
		supplierRepository.save(supplierToSave);
		
		return Response.builder()
				.status(200)
				.message("supplier created successfully")
				.build();
		
	}

	@Override
	public Response getAllSuppliers() {
		
		List<Supplier> suppliers = supplierRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		
		List<SupplierDTO> supplierDTO = modelMapper.map(suppliers, new TypeToken<List<SupplierDTO>>() {}.getType());
		
		return Response.builder()
				.status(200)
				.message("success")
				.suppliers(supplierDTO)
				.build();
		
		
	}

	@Override
	public Response getSupplierById(Long id) {
		
		Supplier supplier = supplierRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("supplier not found"));
		
		SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);
		
		return Response.builder()
				.status(200)
				.message("success")
				.supplier(supplierDTO)
				.build();
		
	}

	@Override
	public Response updateSupplier(Long id, SupplierDTO supplierDTO) {
		
		Supplier existingSupplier = supplierRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("supplier not found"));
		
		
		if(supplierDTO.getName() !=null )existingSupplier.setName(supplierDTO.getName());
		if(supplierDTO.getAddress() !=null )existingSupplier.setName(supplierDTO.getAddress());
		
		supplierRepository.save(existingSupplier);
		
		return Response.builder()
				.status(200)
				.message("supplier successfully updated")
				.supplier(supplierDTO)
				.build();
		
		
		
	}

	@Override
	public Response deleteSupplier(Long id) {
		
		supplierRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("supplier not found"));
		
		supplierRepository.deleteById(id);
		
		return Response.builder()
				.status(200)
				.message("supplier successfully deleted")
				.build();
		
	}

}
