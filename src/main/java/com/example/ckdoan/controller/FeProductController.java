package com.example.ckdoan.controller;

import com.example.ckdoan.model.FeProduct;
import com.example.ckdoan.repository.FeProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
//@RequestMapping("/feproduct")  // Đảm bảo rằng tất cả các route của controller này bắt đầu với /feproduct
public class FeProductController {

    private final FeProductRepository feProductRepository;
    private final String IMAGE_PATH = "D:\\Web1\\KTCK\\ckdoan\\public\\images\\"; // Đảm bảo thư mục này tồn tại

    @Autowired
    public FeProductController(FeProductRepository feProductRepository) {
        this.feProductRepository = feProductRepository;
    }

    // Lấy tất cả sản phẩm (dành cho view)
    @GetMapping("/")
    public String getAllProducts(Model model) {
        List<FeProduct> products = feProductRepository.findAll();
        model.addAttribute("products", products);
        return "index"; // Trả về tên view template (file HTML)
    }

    // Lấy một sản phẩm theo ID
    @GetMapping("/{id}")
    public String getProductById(@PathVariable Long id, Model model) {
        Optional<FeProduct> product = feProductRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-detail";
        } else {
            return "error"; // Trả về view lỗi nếu không tìm thấy sản phẩm
        }
    }

    // Hiển thị form tạo sản phẩm mới
    @GetMapping("/new")
    public String createProductForm(Model model) {
        model.addAttribute("product", new FeProduct());
        return "product-form"; // View template cho form sản phẩm
    }

    // Lưu sản phẩm mới (từ form submission)
    @PostMapping
    public String createProduct(@ModelAttribute FeProduct product,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                product.setImageUrl(imagePath);
            }
            feProductRepository.save(product);
            return "redirect:/feproduct"; // Chuyển hướng lại danh sách sản phẩm
        } catch (IOException e) {
            return "error"; // Trả về lỗi nếu lưu hình ảnh thất bại
        }
    }

    // Hiển thị form cập nhật sản phẩm
    @GetMapping("/edit/{id}")
    public String updateProductForm(@PathVariable Long id, Model model) {
        Optional<FeProduct> product = feProductRepository.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            return "product-form";
        } else {
            return "error";
        }
    }

    // Lưu sản phẩm đã cập nhật
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute FeProduct product,
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = saveImage(imageFile);
                product.setImageUrl(imagePath);
            }
            product.setProductId(id); // Đảm bảo ID không bị thay đổi trong quá trình cập nhật
            feProductRepository.save(product);
            return "redirect:/feproduct";
        } catch (IOException e) {
            return "error"; // Trả về lỗi nếu lưu hình ảnh thất bại
        }
    }

    // Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        feProductRepository.deleteById(id);
        return "redirect:/feproduct";
    }

    // Lưu hình ảnh
    private String saveImage(MultipartFile imageFile) throws IOException {
        String fileName = imageFile.getOriginalFilename();
        String filePath = IMAGE_PATH + fileName;

        // Tạo thư mục nếu nó không tồn tại
        File directory = new File(IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo tất cả các thư mục cần thiết
        }

        // Lưu file vào thư mục
        imageFile.transferTo(new File(filePath));
        return filePath; // Trả về đường dẫn hình ảnh đã lưu
    }
}
