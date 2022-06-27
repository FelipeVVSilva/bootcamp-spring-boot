package com.felipeveiga.fvcatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.fvcatalog.entities.Product;
import com.felipeveiga.fvcatalog.entities.dto.ProductDTO;
import com.felipeveiga.fvcatalog.repositories.ProductRepository;
import com.felipeveiga.fvcatalog.services.exceptions.ObjectNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repo;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(PageRequest pageRequest){
		Page<Product> list = repo.findAll(pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repo.findById(id);
		Product entity = obj.orElseThrow(() -> new ObjectNotFoundException("Id not exist: " + id));
		return new ProductDTO(entity, entity.getCategories());
	}
	
}
