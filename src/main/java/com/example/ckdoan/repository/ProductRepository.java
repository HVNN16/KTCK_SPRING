package com.example.ckdoan.repository;
import com.example.ckdoan.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Rename to ProductRepository
public interface ProductRepository extends JpaRepository<Product, Long> {
}