package test;

import dao.CartDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.CartItem;
import java.util.List;

public class CartDAOTest {

    public static void main(String[] args) {
        CartDAO cartDAO = new CartDAO();

        int testUserId = 5;      // 👈 Put a valid UserID here
        int testProductId = 1;   // 👈 Put a valid ProductID from CartDetails here

        boolean success = cartDAO.removeCartItem(testUserId, testProductId);

        if (success) {
            System.out.println("Cart item with UserID " + testUserId + " and ProductID " + testProductId + " was successfully removed.");
        } else {
            System.out.println("Failed to remove cart item with UserID " + testUserId + " and ProductID " + testProductId + ".");
        }
    }
}
// Test parameters
//        int testUserId = 1; // Use a user ID that exists in your database
//        // Test 1: Get cart items (should return empty list if none exist)
//        System.out.println("Test 1: Get cart items for user " + testUserId);
//        List<CartItem> cartItems = cartDAO.getCartItems(testUserId);
//        System.out.println("Found " + cartItems.size() + " items in cart");
//        for (CartItem item : cartItems) {
//            System.out.println("Item: " + item.getProduct().getName() + 
//                              ", Quantity: " + item.getQuantity() + 
//                              ", Price: " + item.getProduct().getPrice() +
//                              ", Subtotal: " + item.getSubtotal());
//        }
//        // Test 2: Add a cart item
//        System.out.println("\nTest 2: Add a test product to cart");
//        Product testProduct = new Product();
//        testProduct.setId(1); // Assuming this product exists in your database
//        testProduct.setName("Test Product");
//        testProduct.setPrice(100.0);
//        testProduct.setImageUrl("test.jpg");
//
//        // Create a CartItem with the test product and quantity to update
//        CartItem testItem = new CartItem(testProduct, 3); // Quantity to add is 3
//
//        // ResultSet simulation
//        ResultSet rs = null;
//
//        try {
//            // Simulating the retrieval of CartDetailID and current Quantity from the database
//            // Here, I am assuming you already have a cart item in the database with CartDetailID = 1 and Quantity = 2
//            Connection conn = DriverManager.getConnection("jdbc:derby://localhost:1527/product", "user", "pass");
//            String selectQuery = "SELECT CartDetailID, Quantity FROM APP.CartDetails WHERE CartID = ? AND ProductID = ?";
//
//            // Create a scrollable ResultSet
//            PreparedStatement stmt = conn.prepareStatement(selectQuery,
//                    ResultSet.TYPE_SCROLL_INSENSITIVE,
//                    ResultSet.CONCUR_READ_ONLY);
//            stmt.setInt(1, 108); // Example CartID
//            stmt.setInt(2, testProduct.getId()); // Using the product ID
//            rs = stmt.executeQuery();
//
//            // Assuming the cart item is found (CartDetailID = 1)
//            if (rs.next()) {
//                // Now test updating the cart item quantity
//                boolean updateResult = cartDAO.updateCartItemQuantity(rs, testItem);
//                System.out.println("Update result: " + updateResult); // Should print true if successful
//            } else {
//                System.out.println("Cart item not found.");
//            }
//
//            // Optionally, you can verify the updated quantity from the database:
//            if (rs != null) {
//                rs.beforeFirst(); // Reset the ResultSet cursor to the beginning
//                if (rs.next()) {
//                    int updatedQuantity = rs.getInt("Quantity");
//                    System.out.println("Updated Quantity: " + updatedQuantity); // Should print the new quantity
//                }
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Error updating cart item quantity.");
//        } finally {
//            // Clean up resources
//            try {
//                if (rs != null) {
//                    rs.close();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//        // Test 4: Update cart item quantity
//        if (!cartItems.isEmpty()) {
//            int cartDetailId = cartItems.get(0).getId();
//            System.out.println("\nTest 4: Update cart item quantity");
//            boolean updateResult = cartDAO.updateCartItem(cartDetailId, 3);
//            System.out.println("Update result: " + updateResult);
//            
//            // Verify the update
//            cartItems = cartDAO.getCartItems(testUserId);
//            if (!cartItems.isEmpty()) {
//                System.out.println("Updated quantity: " + cartItems.get(0).getQuantity());
//            }
//        }
//        
//        // Test 5: Get cart count
//        System.out.println("\nTest 5: Get cart count");
//        int count = cartDAO.getCartItemCount(testUserId);
//        System.out.println("Cart count: " + count);
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
