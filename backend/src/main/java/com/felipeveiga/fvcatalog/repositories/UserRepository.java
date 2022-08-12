package com.felipeveiga.fvcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipeveiga.fvcatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

}