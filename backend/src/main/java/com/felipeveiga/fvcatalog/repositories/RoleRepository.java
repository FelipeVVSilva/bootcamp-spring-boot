package com.felipeveiga.fvcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipeveiga.fvcatalog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

}
