package com.example.ckdoan.repository;

import com.example.ckdoan.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // Additional custom queries (if needed) can go here
}
