package com.example.ckdoan.service;

import com.example.ckdoan.model.FeProduct;
import java.util.List;
import java.util.Optional;

public interface FeProductService {
    List<FeProduct> getAllProducts();
    Optional<FeProduct> getProductById(Long id);
    FeProduct createProduct(FeProduct product);
    FeProduct updateProduct(Long id, FeProduct product);
    void deleteProduct(Long id);
}
