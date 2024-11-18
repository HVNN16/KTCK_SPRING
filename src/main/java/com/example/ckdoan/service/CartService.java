package com.example.ckdoan.service;

import com.example.ckdoan.model.Cart;
import com.example.ckdoan.model.Product;
import com.example.ckdoan.repository.CartRepository;
import com.example.ckdoan.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    // Add product to cart
    public void addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = new Cart(product, quantity);
        cartRepository.save(cart);
    }

    // Get all items in the cart
    public List<Cart> getCartItems() {
        return cartRepository.findAll(); // Assuming you're saving cart items in a DB
    }

    // Update cart item
    public void updateCartItem(Long cartItemId, int quantity) {
        Cart cartItem = cartRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(quantity)));
        cartRepository.save(cartItem);
    }

    // Remove item from cart
    public void removeFromCart(Long cartItemId) {
        cartRepository.deleteById(cartItemId);
    }

    // Calculate the total price of items in the cart as double
    public double calculateTotal(List<Cart> cartItems) {
        double total = 0.0;
        for (Cart cartItem : cartItems) {
            total += cartItem.getTotalPrice().doubleValue();  // Convert BigDecimal to double and add
        }
        return total;
    }

    // Clear the cart by removing all items
    public void clearCart() {
        cartRepository.deleteAll();  // Clears all cart items
    }

}
