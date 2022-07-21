package com.felipeveiga.fvcatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.felipeveiga.fvcatalog.entities.Product;
import com.felipeveiga.fvcatalog.entities.dto.ProductDTO;
import com.felipeveiga.fvcatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repo;
	
	private long existingId;
	private long notExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		notExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		
		repo.deleteById(existingId);
		
		Optional<Product> result = repo.findById(existingId);
		
		//Assertions.assertFalse(result.isPresent());
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			repo.deleteById(notExistingId);
		});
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repo.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void findByIdShouldFindObjectWhenIdExist() {
		
		ProductDTO productDto = Factory.createProductDTO();
		
		Optional<Product> product = repo.findById(productDto.getId());
		
		Assertions.assertTrue(product.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnNullWhenIdNotExist() {
		
		Optional<Product> product = repo.findById(notExistingId);
		
		Assertions.assertFalse(product.isPresent());
	}
}
