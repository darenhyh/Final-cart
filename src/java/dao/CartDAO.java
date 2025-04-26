package dao;

import model.CartItem;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    
    private static final String JDBC_URL = "jdbc:derby://localhost:1527/product";
    private static final String USERNAME = "userrrrrrrr";
    private static final String PASSWORD = "pass";
    
    // Save cart item to database
    public boolean addCartItem(int userId, CartItem item) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            // Check if user already has a cart
            String cartQuery = "SELECT CartID FROM APP.Cart WHERE UserID = ?";
            stmt = conn.prepareStatement(cartQuery);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            int cartId;
            if (rs.next()) {
                // User has an existing cart
                cartId = rs.getInt("CartID");
            } else {
                // Create a new cart for the user
                String createCartQuery = "INSERT INTO Cart (UserID) VALUES (?)";
                stmt = conn.prepareStatement(createCartQuery, Statement.RETURN_GENERATED_KEYS);
                stmt.setInt(1, userId);
                stmt.executeUpdate();
                
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    cartId = rs.getInt(1);
                } else {
                    return false; // Failed to create cart
                }
            }
            
            // Check if the product is already in the cart
            String checkItemQuery = "SELECT CartDetailID, Quantity FROM APP.CartDetails WHERE CartID = ? AND ProductID = ?";
            stmt = conn.prepareStatement(checkItemQuery);
            stmt.setInt(1, cartId);
            stmt.setInt(2, item.getProduct().getId());
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                // Update existing cart item quantity
                int cartDetailsId = rs.getInt("CartDetailID");
                int currentQuantity = rs.getInt("Quantity");
                int newQuantity = currentQuantity + item.getQuantity();
                
                String updateQuery = "UPDATE APP.CartDetails SET Quantity = ? WHERE CartDetailID = ?";
                stmt = conn.prepareStatement(updateQuery);
                stmt.setInt(1, newQuantity);
                stmt.setInt(2, cartDetailsId);
                stmt.executeUpdate();
            } else {
                // Add new cart item
                String insertQuery = "INSERT INTO APP.CartDetails (CartID, ProductID, Quantity) VALUES (?, ?, ?)";
                stmt = conn.prepareStatement(insertQuery);
                stmt.setInt(1, cartId);
                stmt.setInt(2, item.getProduct().getId());
                stmt.setInt(3, item.getQuantity());
                stmt.executeUpdate();
            }
            
            return true;
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Get all cart items for a user
    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            // Get cart ID for user
            String cartQuery = "SELECT CartID FROM APP.Cart WHERE UserID = ?";
            stmt = conn.prepareStatement(cartQuery);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int cartId = rs.getInt("CartID");
                
                // Get cart items with product information
                String itemsQuery = "SELECT cd.CartDetailID, cd.ProductID, cd.Quantity, " +
                                   "p.PRODUCTNAME, p.DESCRIPTION, p.CATEGORY, p.PRICE, p.STOCK_QUANTITY, p.IMAGE_URL " +
                                   "FROM APP.CartDetails cd " +
                                   "JOIN APP.PRODUCTS p ON cd.ProductID = p.PRODUCT_ID " +
                                   "WHERE cd.CartID = ?";
                                   
                stmt = conn.prepareStatement(itemsQuery);
                stmt.setInt(1, cartId);
                rs = stmt.executeQuery();
                
                while (rs.next()) {
                    // Create Product object
                    Product product = new Product();
                    product.setId(rs.getInt("ProductID"));
                    product.setName(rs.getString("PRODUCTNAME"));
                    product.setDescription(rs.getString("DESCRIPTION"));
                    product.setCategory(rs.getString("CATEGORY"));
                    product.setPrice(rs.getDouble("PRICE"));
                    product.setStock(rs.getInt("STOCK_QUANTITY"));
                    product.setImageUrl(rs.getString("IMAGE_URL"));
                    
                    // Create CartItem object
                    CartItem item = new CartItem();
                    item.setId(rs.getInt("CartDetailID"));
                    item.setProduct(product);
                    item.setQuantity(rs.getInt("Quantity"));
                    
                    cartItems.add(item);
                }
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return cartItems;
    }
    
    // Update cart item quantity
    public boolean updateCartItem(int cartDetailsId, int quantity) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            String updateQuery = "UPDATE APP.CartDetails SET Quantity = ? WHERE CartDetailID = ?";
            stmt = conn.prepareStatement(updateQuery);
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartDetailsId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Remove item from cart
    public boolean removeCartItem(int cartDetailsId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            String deleteQuery = "DELETE FROM APP.CartDetails WHERE CartDetailID = ?";
            stmt = conn.prepareStatement(deleteQuery);
            stmt.setInt(1, cartDetailsId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Clear all items from a user's cart
    public boolean clearCart(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            // Get cart ID for user
            String cartQuery = "SELECT CartID FROM APP.Cart WHERE UserID = ?";
            stmt = conn.prepareStatement(cartQuery);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int cartId = rs.getInt("CartID");
                
                // Delete all items in the cart
                String deleteQuery = "DELETE FROM APP.CartDetails WHERE CartID = ?";
                stmt = conn.prepareStatement(deleteQuery);
                stmt.setInt(1, cartId);
                stmt.executeUpdate();
                
                return true;
            }
            
            return false;
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Get cart count for a user
    public int getCartItemCount(int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int itemCount = 0;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            // Get cart ID for user
            String cartQuery = "SELECT CartID FROM APP.Cart WHERE UserID = ?";
            stmt = conn.prepareStatement(cartQuery);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                int cartId = rs.getInt("CartID");
                
                // Count items in cart
                String countQuery = "SELECT SUM(Quantity) as TotalItems FROM APP.CartDetails WHERE CartID = ?";
                stmt = conn.prepareStatement(countQuery);
                stmt.setInt(1, cartId);
                rs = stmt.executeQuery();
                
                if (rs.next()) {
                    itemCount = rs.getInt("TotalItems");
                }
            }
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return itemCount;
    }
}