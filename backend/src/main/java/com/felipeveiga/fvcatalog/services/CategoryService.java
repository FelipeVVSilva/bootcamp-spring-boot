package com.felipeveiga.fvcatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.fvcatalog.entities.Category;
import com.felipeveiga.fvcatalog.entities.dto.CategoryDTO;
import com.felipeveiga.fvcatalog.repositories.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(PageRequest pageRequest){
		Page<Category> list = repo.findAll(pageRequest);
		return list.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repo.findById(id);
		Category entity = obj.get();
		return new CategoryDTO(entity);
	}
	
}
