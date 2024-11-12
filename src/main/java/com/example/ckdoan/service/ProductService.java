package com.example.ckdoan.service;

import com.example.ckdoan.model.Product;
import com.example.ckdoan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp; // Sửa đổi ở đây
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
    public Product findById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
    }


    // Save new or updated product
    public Product save(Product product) {
        // Set timestamps
        if (product.getCreatedAt() == null) {
            product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }
        product.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        return productRepository.save(product);
    }

    // Delete product by ID
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }
}
