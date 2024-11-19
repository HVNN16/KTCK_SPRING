package com.example.ckdoan.controller;

import com.example.ckdoan.model.Product;
import com.example.ckdoan.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    // Path to save images
    private final String IMAGE_PATH = "D:\\Web1\\KTCK\\ckdoan\\public\\images\\"; // Ensure the directory exists

    // Display product list
    @GetMapping({"", "/shop"})
    public String showProductList(Model model, @SessionAttribute(name = "cart", required = false) List<Product> cart) {
        List<Product> products = productService.findAll(); // Use service method instead of repo directly
        model.addAttribute("products", products);
        model.addAttribute("cart", cart); // Display cart contents on the product page
        return "shop"; // Return to the shop view
    }

    // Show product details
    @GetMapping("/detail")
    public String showDetailPage(Model model) {
        List<Product> products = productService.findAll(); // Fetch all products
        model.addAttribute("products", products); // Add the list of products to the model
        return "detail"; // Return to the detail view
    }

    // Add product to the cart (session-based)
    @PostMapping("/add-to-cart/{productId}")
    public String addToCart(@PathVariable("productId") Long productId, @SessionAttribute(name = "cart", required = false) List<Product> cart, HttpSession session) {
        Product product = productService.findById(productId);
        if (product != null) {
            if (cart == null) {
                cart = new ArrayList<>();
            }
            cart.add(product);
            session.setAttribute("cart", cart); // Store updated cart in the session
        }
        return "redirect:/cart"; // Redirect to the cart page
    }

//    // Show cart page with the products added to the cart
//    @GetMapping("/cart")
//    public String showCartPage(@SessionAttribute(name = "cart", required = false) List<Product> cart, Model model) {
//        model.addAttribute("cart", cart);
//        return "cart"; // Display the cart page
//    }

    // Get all products (for API)
    @GetMapping("/products")
    @ResponseBody
    public List<Product> getProductList() {
        return productService.findAll();
    }

    // Get product by ID (for API)
    @GetMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        Product product = productService.findById(productId);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    // Create new product
    @PostMapping("/products")
    @ResponseBody
    public ResponseEntity<Product> createProduct(@RequestParam("product") String productJson,
                                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            Product product = new ObjectMapper().readValue(productJson, Product.class);
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                product.setImageUrl(imagePath);
            }
            productService.save(product);
            return ResponseEntity.status(201).body(product); // Return 201 for created resource
        } catch (IOException e) {
            return ResponseEntity.badRequest().build(); // Return 400 for bad request
        }
    }

    // Update product by ID
    @PutMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId,
                                                 @RequestParam("product") String productJson,
                                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Product existingProduct = productService.findById(productId);
        if (existingProduct != null) {
            try {
                Product updateProduct = new ObjectMapper().readValue(productJson, Product.class);
                existingProduct.setProductName(updateProduct.getProductName());
                existingProduct.setDescription(updateProduct.getDescription());
                existingProduct.setPrice(updateProduct.getPrice());
                existingProduct.setStock(updateProduct.getStock());

                if (imageFile != null && !imageFile.isEmpty()) {
                    String imagePath = saveImage(imageFile);
                    existingProduct.setImageUrl(imagePath);
                }

                productService.save(existingProduct);
                return ResponseEntity.ok(existingProduct); // Return updated product
            } catch (IOException e) {
                return ResponseEntity.badRequest().build(); // Return 400 if error in parsing JSON
            }
        }
        return ResponseEntity.status(404).build(); // Return 404 if product not found
    }

    // Delete product by ID
    @DeleteMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Void> removeProductById(@PathVariable("id") Long productId) {
        try {
            productService.delete(productId); // Delete the product
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 if product not found
        }
    }

    // Admin page to manage products
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "admin"; // Return view for admin management
    }

    // Save the uploaded image
    private String saveImage(MultipartFile imageFile) {
        String fileName = imageFile.getOriginalFilename();
        String filePath = IMAGE_PATH + fileName; // Full path to save image

        // Ensure the directory exists
        File directory = new File(IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if not exists
        }

        try {
            imageFile.transferTo(new File(filePath)); // Save file to path
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
        return "images/" + fileName; // Return relative path to access image via URL
    }
    //Checkout Template
    @GetMapping("/checkout")
    public String showCheckoutPage() {
        return "checkout"; // Tên file HTML trong thư mục templates (không cần đuôi .html)
    }
    //Contact Template
    @GetMapping("/contact")
    public String showContactPage() {
        return "contact"; // Tên file HTML trong thư mục templates (không cần đuôi .html)
    }
    //Register Template
    @GetMapping("/register")
    public String showResigterPage() {
        return "register"; // Tên file HTML trong thư mục templates (không cần đuôi .html)
    }
}
