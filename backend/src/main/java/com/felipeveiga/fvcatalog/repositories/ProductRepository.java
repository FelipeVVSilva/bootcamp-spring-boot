package com.felipeveiga.fvcatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipeveiga.fvcatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

}
