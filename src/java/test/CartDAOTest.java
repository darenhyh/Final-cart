package test;

import dao.CartDAO;
import model.CartItem;
import model.Product;
import java.util.List;

public class CartDAOTest {
    
    public static void main(String[] args) {
        // Create a test instance of CartDAO
        CartDAO cartDAO = new CartDAO();
        
        // Test parameters
        int testUserId = 1; // Use a user ID that exists in your database
        
        // Test 1: Get cart items (should return empty list if none exist)
        System.out.println("Test 1: Get cart items for user " + testUserId);
        List<CartItem> cartItems = cartDAO.getCartItems(testUserId);
        System.out.println("Found " + cartItems.size() + " items in cart");
        for (CartItem item : cartItems) {
            System.out.println("Item: " + item.getProduct().getName() + 
                              ", Quantity: " + item.getQuantity() + 
                              ", Price: " + item.getProduct().getPrice() +
                              ", Subtotal: " + item.getSubtotal());
        }
        
        // Test 2: Add a cart item
        System.out.println("\nTest 2: Add a test product to cart");
        Product testProduct = new Product();
        testProduct.setId(1); // Use a product ID that exists in your database
        testProduct.setName("Test Product");
        testProduct.setPrice(100.0);
        testProduct.setImageUrl("test.jpg");
        
        CartItem testItem = new CartItem(testProduct, 1);
        boolean addResult = cartDAO.addCartItem(testUserId, testItem);
        System.out.println("Add result: " + addResult);
        
        // Test 3: Get cart items again to verify addition
        System.out.println("\nTest 3: Get cart items again to verify addition");
        cartItems = cartDAO.getCartItems(testUserId);
        System.out.println("Found " + cartItems.size() + " items in cart");
        for (CartItem item : cartItems) {
            System.out.println("Item: " + item.getProduct().getName() + 
                              ", Quantity: " + item.getQuantity() + 
                              ", Price: " + item.getProduct().getPrice() +
                              ", Subtotal: " + item.getSubtotal());
        }
        
        // Test 4: Update cart item quantity
        if (!cartItems.isEmpty()) {
            int cartDetailId = cartItems.get(0).getId();
            System.out.println("\nTest 4: Update cart item quantity");
            boolean updateResult = cartDAO.updateCartItem(cartDetailId, 3);
            System.out.println("Update result: " + updateResult);
            
            // Verify the update
            cartItems = cartDAO.getCartItems(testUserId);
            if (!cartItems.isEmpty()) {
                System.out.println("Updated quantity: " + cartItems.get(0).getQuantity());
            }
        }
        
        // Test 5: Get cart count
        System.out.println("\nTest 5: Get cart count");
        int count = cartDAO.getCartItemCount(testUserId);
        System.out.println("Cart count: " + count);
        
        // Test 6: Remove an item
        // Uncomment this to test removal if needed
        
        /*if (!cartItems.isEmpty()) {
            int cartDetailId = cartItems.get(0).getId();
            System.out.println("\nTest 6: Remove cart item");
            boolean removeResult = cartDAO.removeCartItem(cartDetailId);
            System.out.println("Remove result: " + removeResult);
            
            // Verify the removal
            cartItems = cartDAO.getCartItems(testUserId);
            System.out.println("Items after removal: " + cartItems.size());
        }
        
        
        // Test 7: Clear cart
        // Uncomment this to test cart clearing if needed
        
        System.out.println("\nTest 7: Clear cart");
        boolean clearResult = cartDAO.clearCart(testUserId);
        System.out.println("Clear result: " + clearResult);
        
        // Verify the clear
        cartItems = cartDAO.getCartItems(testUserId);
        System.out.println("Items after clearing: " + cartItems.size());*/
        
    }
}