package com.felipeveiga.fvcatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.fvcatalog.entities.dto.ProductDTO;
import com.felipeveiga.fvcatalog.repositories.ProductRepository;
import com.felipeveiga.fvcatalog.services.exceptions.ObjectNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;
	@Autowired
	private ProductRepository repo;
	
	private Long existindId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existindId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void deleteShouldDeleteResouceWhenIdExists() {
		
		service.delete(existindId);
		
		Assertions.assertEquals(countTotalProducts - 1, repo.count());
		
	}
	
	@Test
	public void deleteShouldThrowObjectNorFoundExceptionWhenIdNotExists() {
		
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		
	}
	
	@Test
	public void findAllShouldReturnPageWhenPage0ZSize10() {
		
		PageRequest pageRequest = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAll(pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	
}
