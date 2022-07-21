package com.felipeveiga.fvcatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.fvcatalog.entities.Category;
import com.felipeveiga.fvcatalog.entities.Product;
import com.felipeveiga.fvcatalog.entities.dto.CategoryDTO;
import com.felipeveiga.fvcatalog.entities.dto.ProductDTO;
import com.felipeveiga.fvcatalog.repositories.CategoryRepository;
import com.felipeveiga.fvcatalog.repositories.ProductRepository;
import com.felipeveiga.fvcatalog.services.exceptions.DatabaseException;
import com.felipeveiga.fvcatalog.services.exceptions.ObjectNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;
	@Autowired
	private CategoryRepository catRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageRequest){
		Page<Product> list = repo.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repo.findById(id);
		Product entity = obj.orElseThrow(() -> new ObjectNotFoundException("Id not exist: " + id));
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repo.save(entity);
		return new ProductDTO(entity, entity.getCategories());
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repo.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repo.save(entity);
			return new ProductDTO(entity, entity.getCategories());
		}
		catch(EntityNotFoundException e) {
			throw new ObjectNotFoundException("Id not exist: " + id);
		}
	}

	@Transactional
	public void delete(Long id) {
		try {
			repo.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ObjectNotFoundException("Id not exist: " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		
		entity.setName(dto.getName());
		entity.setPrice(dto.getPrice());
		entity.setDescription(dto.getDescription());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
		
		entity.getCategories().clear();
		
		for(CategoryDTO catDto : dto.getCategories()) {
			Category category = catRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
		
	}
}
