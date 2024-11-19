package com.example.ckdoan.controller;

import com.example.ckdoan.model.Cart;
import com.example.ckdoan.service.CartService;
import com.example.ckdoan.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, @RequestParam int quantity, Model model) {
        cartService.addToCart(productId, quantity);
        // Add cart to model
        model.addAttribute("cart", cartService.getCartItems());
        return "redirect:/cart/view"; // Redirect to view cart page
    }

    @GetMapping("/view")
    public String viewCart(Model model) {
        // Add cart to model
        model.addAttribute("cart", cartService.getCartItems());
        return "cart"; // Return the cart view
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCartItem(@RequestParam Long cartItemId, @RequestParam int quantity) {
        cartService.updateCartItem(cartItemId, quantity);
        List<Cart> cartItems = cartService.getCartItems();
        double subtotal = cartService.calculateTotal(cartItems);
        double total = subtotal + 10; // Add shipping cost
        Map<String, Object> response = new HashMap<>();
        response.put("cartItems", cartItems);
        response.put("subtotal", subtotal);
        response.put("total", total);
        return response;
    }

    @PostMapping("/remove")
    @ResponseBody
    public Map<String, Object> removeFromCart(@RequestParam Long cartItemId) {
        cartService.removeFromCart(cartItemId);
        List<Cart> cartItems = cartService.getCartItems();
        double subtotal = cartService.calculateTotal(cartItems);
        double total = subtotal + 10; // Add shipping cost
        Map<String, Object> response = new HashMap<>();
        response.put("cartItems", cartItems);
        response.put("subtotal", subtotal);
        response.put("total", total);
        return response;
    }


}
