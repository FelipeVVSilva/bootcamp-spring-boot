package com.felipeveiga.fvcatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.fvcatalog.entities.Role;
import com.felipeveiga.fvcatalog.entities.User;
import com.felipeveiga.fvcatalog.entities.dto.RoleDTO;
import com.felipeveiga.fvcatalog.entities.dto.UserDTO;
import com.felipeveiga.fvcatalog.entities.dto.UserInsertDTO;
import com.felipeveiga.fvcatalog.entities.dto.UserUpdateDTO;
import com.felipeveiga.fvcatalog.repositories.RoleRepository;
import com.felipeveiga.fvcatalog.repositories.UserRepository;
import com.felipeveiga.fvcatalog.services.exceptions.DatabaseException;
import com.felipeveiga.fvcatalog.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repo;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable){
		Page<User> list = repo.findAll(pageable);
		return list.map(x -> new UserDTO(x, x.getRoles()));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repo.findById(id);
		User entity = obj.orElseThrow(() -> new ObjectNotFoundException("Id not exist: " + id));
		return new UserDTO(entity, entity.getRoles());
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(encoder.encode(dto.getPassword()));
		entity = repo.save(entity);
		return new UserDTO(entity, entity.getRoles());
	}
	
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repo.getOne(id);
			copyDtoToEntity(dto, entity);
			entity = repo.save(entity);
			return new UserDTO(entity, entity.getRoles());
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
	
	private void copyDtoToEntity(UserDTO dto, User entity) {
		
		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		
		entity.getRoles().clear();
		
		for(RoleDTO roleDto : dto.getRoles()) {
			Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}	
	}
}
