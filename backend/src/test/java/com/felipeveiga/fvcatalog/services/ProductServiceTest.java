package com.felipeveiga.fvcatalog.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.felipeveiga.fvcatalog.entities.Category;
import com.felipeveiga.fvcatalog.entities.Product;
import com.felipeveiga.fvcatalog.entities.dto.ProductDTO;
import com.felipeveiga.fvcatalog.repositories.CategoryRepository;
import com.felipeveiga.fvcatalog.repositories.ProductRepository;
import com.felipeveiga.fvcatalog.services.exceptions.DatabaseException;
import com.felipeveiga.fvcatalog.services.exceptions.ObjectNotFoundException;
import com.felipeveiga.fvcatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repo;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	private ProductDTO productDto;
	
	@BeforeEach
	void setup() throws Exception {
		
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category = Factory.createCategory();
		productDto = Factory.createProductDTO();
		
		Mockito.when(repo.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repo.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repo.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repo.findById(nonExistingId)).thenThrow(ObjectNotFoundException.class);
		
		Mockito.when(repo.getOne(existingId)).thenReturn(product);
		Mockito.when(repo.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
		Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		Mockito.doNothing().when(repo).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repo).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repo).deleteById(dependentId);
	}
	
	@Test
	public void deleteSholdDoNothingWhenIdExist() {
		
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		
		Mockito.verify(repo, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteSholdThrowObjectNotFoundExceptionWhenIdNotExist() {
		
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		
		Mockito.verify(repo, Mockito.times(1)).deleteById(nonExistingId);
	}
	
	@Test
	public void deleteSholdThrowDatabaseExceptionWhenIdIsDependent() {
		
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		
		Mockito.verify(repo, Mockito.times(1)).deleteById(dependentId);
	}
	
	@Test
	public void findAllShouldReturnPage() {
		
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAll(pageable);
		
		Assertions.assertNotNull(result);
		Mockito.verify(repo, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExist() {
		
		ProductDTO productDto = service.findById(existingId);
		
		//Assertions.assertEquals(1L, productDto.getId());
		//Assertions.assertEquals(ProductDTO.class, productDto.getClass());
		Assertions.assertNotNull(productDto);
		
	}
	
	@Test
	public void findByIdShouldThrowObjectNotFoundExceptionWhenIdDoesNotExist() {
		
		//ProductDTO productDto = service.findById(nonExistingId);
		
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		
	}
	
	@Test
	public void updateShouldUpdateWhenIdExist() {
		
		ProductDTO result = service.update(existingId, productDto);
		
		Assertions.assertNotNull(result);
		
		
	}
	
	@Test
	public void updateShouldThrowObjectNotFoundExceptionWhenIdNotExist() {
		
		Assertions.assertThrows(ObjectNotFoundException.class, () -> {
			service.update(nonExistingId, productDto);
		});
		
	}
	
}
