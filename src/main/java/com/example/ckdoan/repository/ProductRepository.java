package com.example.ckdoan.repository;

import com.example.ckdoan.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

// Giao diện ProductRepository kế thừa JpaRepository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository cung cấp các phương thức CRUD cơ bản cho entity Product
    // Bạn có thể thêm các phương thức tùy chỉnh tại đây nếu cần
}
