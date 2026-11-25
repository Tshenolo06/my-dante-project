package com.mycompany.inventorymanagementservice.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mycompany.inventorymanagementservice.dto.CategoryDTO;
import com.mycompany.inventorymanagementservice.dto.ProductDTO;
import com.mycompany.inventorymanagementservice.dto.Response;
import com.mycompany.inventorymanagementservice.dto.UserDTO;
import com.mycompany.inventorymanagementservice.entity.Category;
import com.mycompany.inventorymanagementservice.entity.Product;
import com.mycompany.inventorymanagementservice.exception.NotFoundException;
import com.mycompany.inventorymanagementservice.repository.CategoryRepository;
import com.mycompany.inventorymanagementservice.repository.ProductRepository;
import com.mycompany.inventorymanagementservice.repository.SupplierRepository;
import com.mycompany.inventorymanagementservice.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService{
	
	private final ProductRepository productRepository;
	
	private final ModelMapper modelMapper;
	
	private final CategoryRepository categoryRepository;
	
	private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-image/";
	
	//AFTER YOUR FROTEND IS SET UP WROTE THIS SO THE IMAGE IS SAVED IN YOUR FRONTEND PUBLIC FOLDER
    private static final String IMAGE_DIRECTOR_FRONTEND = "Users/tshen/ims-angular/public/products/";
    

	@Override
	public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {
		
		Category category = categoryRepository.findById(productDTO.getCategoryId())
				.orElseThrow(()-> new NotFoundException("Category not found"));
		
		Product productToSave = Product.builder()
										.name(productDTO.getName())
										.sku(productDTO.getSku())
										.price(productDTO.getPrice())
										.stockQuantity(productDTO.getStockQuantity())
										.description(productDTO.getDescription())
										.category(category)
										.build();
		
						if(imageFile != null) {
							
							String imagePath = saveImage(imageFile);
							productToSave.setImageUrl(imagePath);	
						}
						
						
		//save the product to our database		
						productRepository.save(productToSave);
						
						
			return Response.builder()
					.status(200)
					.message("Product saved successfully")
					.build();
	}

	@Override
	public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
		
				Product existingProduct = productRepository.findById(productDTO.getProductId())
						.orElseThrow(()-> new NotFoundException("Product not found"));
		
				if(imageFile != null && !imageFile.isEmpty()) {
					
					String imagePath = saveImage(imageFile);
					existingProduct.setImageUrl(imagePath);	
				}
		//check if category is to be changed for the product
		if(productDTO.getCategoryId() !=null && productDTO.getCategoryId() > 0) {
			
			Category category = categoryRepository.findById(productDTO.getProductId())
					.orElseThrow(()-> new NotFoundException("Category not found"));
			existingProduct.setCategory(category);
		}
		
		//check and update fields
		if(productDTO.getName() !=null && productDTO.getName().isBlank()) {
			
			existingProduct.setName(productDTO.getName());
			
		}
		
		if(productDTO.getSku() !=null && productDTO.getSku().isBlank()) {
			
			existingProduct.setSku(productDTO.getSku());
			
		}
		
		if(productDTO.getDescription() !=null && productDTO.getDescription().isBlank()) {
			
			existingProduct.setDescription(productDTO.getDescription());
			
		}
	
		if(productDTO.getPrice() !=null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
			
			existingProduct.setPrice(productDTO.getPrice());;
			
		}
		
		if(productDTO.getStockQuantity() !=null && productDTO.getStockQuantity() >= 0) {
			
			existingProduct.setPrice(productDTO.getPrice());
			
		}
		
		productRepository.save(existingProduct);
		
		return Response.builder()
				.status(200)
				.message("Product updated successfully")
				.build();
		
	}

	@Override
	public Response getAllProducts() {
		List<Product> products = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
		
		List<ProductDTO> productDTO = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());
		
		return Response.builder()
				.status(200)
				.message("success")
				.products(productDTO)
				.build();
	}

	@Override
	public Response getProductById(Long id) {
		
		Product products = productRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("Product not found"));
		
		return Response.builder()
				.status(200)
				.message("success")
				.product(modelMapper.map(products, ProductDTO.class))
				.build();
	}

	@Override
	public Response deleteProduct(Long id) {
		
		productRepository.findById(id)
				.orElseThrow(()-> new NotFoundException("Product not found"));
		
		productRepository.deleteById(id);
		
		return Response.builder()
				.status(200)
				.message("Product successfully deleted")
				.build();
	}
	
	private String saveImageToFrontendPublicFolder(MultipartFile imageFile){
        //validate image check
        if (!imageFile.getContentType().startsWith("image/")){
            throw new IllegalArgumentException("Only image files are allowed");
        }
        //create the directory to store images if it doesn't exist
        File directory = new File(IMAGE_DIRECTOR_FRONTEND);

        if (!directory.exists()){
            directory.mkdir();
            log.info("Directory was created");
        }
        //generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        //get the absolute path of the image
        String imagePath = IMAGE_DIRECTOR_FRONTEND + uniqueFileName;

        try {
            File desctinationFile = new File(imagePath);
            imageFile.transferTo(desctinationFile); //we are transfering(writing to this folder)

        }catch (Exception e){
            throw new IllegalArgumentException("Error occurend while saving image" + e.getMessage());
        }

        return "products/"+uniqueFileName;
    }
	
	
	private String saveImage(MultipartFile imageFile) {
		
		//validate image file
		if(!imageFile.getContentType().startsWith("image/")) {
			
			throw new IllegalArgumentException("only image file are allowed");
			
		}
		
		//create a directory to validate image if it does not exist
		File directory = new File(IMAGE_DIRECTORY);
		
		if(!directory.exists()) {
			
			directory.mkdir();
			log.info("Directory was created");
			
		}
		
		//generate unique filename for the image
		String uniqueFileName = UUID.randomUUID() +"_"+ imageFile.getOriginalFilename();
		
		//get the absolute path of the image
		String imagePath = IMAGE_DIRECTORY + uniqueFileName;
		
		try {
			File destinationPath = new File(imagePath);
			
			imageFile.transferTo(destinationPath);
			
		}catch(Exception e) {
			
			throw new IllegalArgumentException("Error occured while saving image" + e.getMessage());
			
			
			
		}
		
		return imagePath;
		
		
	}

}
