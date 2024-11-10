package com.example.ckdoan.controller;

import com.example.ckdoan.model.Product;
import com.example.ckdoan.repository.ProductRepository;
import com.example.ckdoan.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository repo;

    // Thay đổi đường dẫn lưu hình ảnh thành đường dẫn hợp lệ và đảm bảo thư mục tồn tại
    private final String IMAGE_PATH = "D:\\Web1\\KTCK\\ckdoan\\public\\images\\"; // Đảm bảo thư mục này tồn tại

    // LAY DU LIEU Tu myphpadmin
    @GetMapping({"", "/shop"})
    public String showProductList(Model model) {
        List<Product> products = repo.findAll();
        model.addAttribute("products", products);
        return "shop"; // Trả về view cho trang shop
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
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long productId) {
        Product product = productService.findById(productId);
        return ResponseEntity.ok(product); // Trả về sản phẩm nếu tìm thấy
    }

    // CREATE NEW PRODUCT
    @PostMapping("/products")
    @ResponseBody
    public ResponseEntity<Product> createProduct(@RequestParam("product") String productJson,
                                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Product product = null;
        try {
            product = new ObjectMapper().readValue(productJson, Product.class);
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                product.setImageUrl(imagePath);
            }
            productService.save(product);
            return ResponseEntity.status(201).body(product);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // UPDATE PRODUCT BY ID
    @PutMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long productId,
                                                 @RequestParam("product") String productJson,
                                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Product product = productService.findById(productId);
        if (product != null) {
            try {
                Product updateProduct = new ObjectMapper().readValue(productJson, Product.class);
                product.setProductName(updateProduct.getProductName());
                product.setDescription(updateProduct.getDescription());
                product.setPrice(updateProduct.getPrice());
                product.setStock(updateProduct.getStock());

                if (imageFile != null && !imageFile.isEmpty()) {
                    String imagePath = saveImage(imageFile);
                    product.setImageUrl(imagePath);
                }

                productService.save(product);
                return ResponseEntity.ok(product); // Trả về sản phẩm đã cập nhật
            } catch (IOException e) {
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.status(404).build(); // Trả về 404 nếu không tìm thấy
    }

    // DELETE PRODUCT BY ID
    @DeleteMapping("/products/{id}")
    @ResponseBody
    public ResponseEntity<Void> removeProductById(@PathVariable("id") Long productId) {
        try {
            productService.delete(productId); // Nếu sản phẩm không tồn tại, sẽ ném ra RuntimeException
            return ResponseEntity.noContent().build(); // Trả về 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Trả về 404 nếu sản phẩm không tồn tại
        }
    }

    // Trang quản lý sản phẩm
    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "admin"; // Trả về view admin.html
    }

    // Lưu hình ảnh
    private String saveImage(MultipartFile imageFile) {
//        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        String fileName = imageFile.getOriginalFilename();
        String filePath = fileName;

        // Tạo thư mục nếu nó không tồn tại
        File directory = new File(IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo tất cả các thư mục cần thiết
        }

        try {
            imageFile.transferTo(new File(filePath)); // Lưu file vào đường dẫn
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
        return filePath; // Trả về đường dẫn hình ảnh đã lưu
    }

//    @GetMapping("/")
//    public String showShopPage() {
//        return "index";
//    }

    @GetMapping("/cart")
    public String showCartPage() {
        return "cart";
    }

    @GetMapping("/register")
    public String showLoginPage() {
        return "register";
    }

    @GetMapping("/detail")
    public String showDetailPage() {
        return "detail";
    }
}
