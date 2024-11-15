package com.example.ckdoan.repository;

import com.example.ckdoan.model.FeProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeProductRepository extends JpaRepository<FeProduct, Long> {
    // Custom queries can be defined here if necessary
}
