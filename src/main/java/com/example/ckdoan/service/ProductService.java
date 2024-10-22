package com.example.ckdoan.service;
import com.example.ckdoan.model.Product;
import com.example.ckdoan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Find all products
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Find product by ID
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Save new or updated product
    public Product save(Product product) {
        return productRepository.save(product);
    }

    // Update product (this is handled by save method since save can insert or update)
    public Product update(Product product) {
        return productRepository.save(product);
    }

    // Delete product by ID
    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}