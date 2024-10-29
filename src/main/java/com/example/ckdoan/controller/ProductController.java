package com.example.ckdoan.controller;
import com.example.ckdoan.model.Product;
import com.example.ckdoan.repository.ProductRepository;
import com.example.ckdoan.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    public ProductController() {
    }
    @Autowired
    private ProductRepository repo;
//    LAY DU LIEU Tu myphpadmin
    @GetMapping({"", "/shop"})
    public String ShowProductList(Model model) {
        List<Product> products = repo.findAll();//Sort.by(Sort.Direction.DESC, "id")
        model.addAttribute("products", products);
        return "shop";
    }
    // GET ALL PRODUCTS
    @GetMapping("/products")
    @ResponseBody
    public List<Product> getProductList() {
        return productService.findAll();
    }

    // GET PRODUCT BY ID
    @GetMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProductById(@PathVariable("id") int productId) {
        Product product = productService.findById((long) productId);
        if (product != null) {
            return ResponseEntity.status(200).body(product);
        }
        return ResponseEntity.status(404).body(null);
    }

    // CREATE NEW PRODUCT
    @PostMapping("/products")
    @ResponseBody
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        productService.save(product);
        return ResponseEntity.status(201).body(product);
    }

    // UPDATE PRODUCT BY ID
    @PutMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> updateProduct(@PathVariable("id") int productId, @RequestBody Product updateProduct) {
        Product product = productService.findById((long) productId);
        if (product != null) {
            product.setProductName(updateProduct.getProductName());
            product.setDescription(updateProduct.getDescription());
            product.setPrice(updateProduct.getPrice());
            product.setStock(updateProduct.getStock());
            product.setCategoryId(updateProduct.getCategoryId());
            product.setImageUrl(updateProduct.getImageUrl());
            productService.save(product);
            return ResponseEntity.status(200).body(product);
        }
        return ResponseEntity.status(404).body(null);
    }

    // DELETE PRODUCT BY ID
    @DeleteMapping("/products/{id}")
    @ResponseBody
    public List<Product> removeProductById(@PathVariable("id") int productId) {
        productService.delete((long) productId);
        return productService.findAll();
    }
    @GetMapping("/")
    public String showShopPage() {
        return "index";
    }
    @GetMapping("/cart")
    public String showCartPage() {
        return "cart";
    }
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "login"
//    }
}
