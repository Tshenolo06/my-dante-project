package com.mycompany.inventorymanagementservice.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.Category;
import com.mycompany.inventorymanagementservice.entity.User;
import com.mycompany.inventorymanagementservice.exception.NotFoundException;
import com.mycompany.inventorymanagementservice.repository.CategoryRepository;
import com.mycompany.inventorymanagementservice.repository.UserRepository;
import com.mycompany.inventorymanagementservice.security.JwtUtils;
import com.mycompany.inventorymanagementservice.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService{
	
	private final CategoryRepository categoryRepository;
	
	private final ModelMapper modelMapper;

	@Override
	public Response createCategory(CategoryDTO categoryDTO) {
		
		Category categoryToSave = modelMapper.map(categoryDTO, Category.class);
		categoryRepository.save(categoryToSave);
		
		return Response.builder()
				.status(200)
				.message("user created successfully")
				.build();
		
	}

	@Override
	public Response getAllCategories() {
		
		List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		
		List<CategoryDTO> categoryDTO = modelMapper.map(categories, new TypeToken<List<UserDTO>>() {}.getType());
		
		return Response.builder()
				.status(200)
				.message("success")
				.categories(categoryDTO)
				.build();
		
		
	}

	@Override
	public Response getCategoryById(Long id) {
		
		Category category = categoryRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("category not found"));
		
		CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
		
		return Response.builder()
				.status(200)
				.message("success")
				.category(categoryDTO)
				.build();
		
	}

	@Override
	public Response updateCategory(Long id, CategoryDTO categoryDTO) {
		
		Category existingCategory = categoryRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("category not found"));
		
		existingCategory.setName(categoryDTO.getName());
		categoryRepository.save(existingCategory);
		
		return Response.builder()
				.status(200)
				.message("category successfully updated")
				.category(categoryDTO)
				.build();
		
		
		
	}

	@Override
	public Response deleteCategory(Long id) {
		
		categoryRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("category not found"));
		
		categoryRepository.deleteById(id);
		
		return Response.builder()
				.status(200)
				.message("category successfully deleted")
				.build();
		
	}

}
