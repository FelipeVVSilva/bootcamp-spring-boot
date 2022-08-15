package com.felipeveiga.fvcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.felipeveiga.fvcatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	@Transactional(readOnly = true)
	@Query(value = "select * from tb_user where email = :email", nativeQuery = true)
	User findByEmail(@Param("email") String email);
	
}
