package com.mycompany.inventorymanagementservice.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.mycompany.inventorymanagementservice.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.transaction.Transaction;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="Categories")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "name is required")
	@Column(unique = true)
	private String name;
	
	@OneToMany(mappedBy = "category")
	private List<Product> transactions;

	@Override
	public String toString() {
		return "Category [id=" + id + ", name=" + name + "]";
	}
	

}
