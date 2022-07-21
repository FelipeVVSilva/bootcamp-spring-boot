package com.felipeveiga.fvcatalog.tests;

import java.time.Instant;

import com.felipeveiga.fvcatalog.entities.Category;
import com.felipeveiga.fvcatalog.entities.Product;
import com.felipeveiga.fvcatalog.entities.dto.ProductDTO;

public class Factory {

	public static Product createProduct() {
		
		Product product = new Product(1L, "Phone", "Good Phone", 800.00, "https://img.com/img.png", Instant.parse("2020-07-13T20:50:07.12345Z"));
		product.getCategories().add(createCategory());
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(1L, "Livros");
	}
	
}
