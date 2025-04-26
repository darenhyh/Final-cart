import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.CartItem;
import model.BuyerDetail;
import model.Address;
import model.PaymentMethod;

@WebServlet("/OrderConfirmedServlet")
public class OrderConfirmedServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        try {
            // 1. Get all data from session
            List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cart");
            Double subtotal = (Double) session.getAttribute("subtotal");
            Double taxAmount = (Double) session.getAttribute("taxAmount");
            Double deliveryFee = (Double) session.getAttribute("deliveryFee");
            Double totalAmount = (Double) session.getAttribute("totalAmount");
            Integer paymentId = (Integer) session.getAttribute("paymentId");
            Integer shippingId = (Integer) session.getAttribute("shippingId");
            
            // Validate required data
            if (cartItems == null || cartItems.isEmpty()) {
                throw new Exception("Cart is empty");
            }
            if (paymentId == null || shippingId == null) {
                throw new Exception("Payment or shipping information missing");
            }
            
            // 2. Generate order details
            String orderId = generateOrderId();
            String orderDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String userId = (String) session.getAttribute("userId");
            if (userId == null) userId = "guest";
            
            // 3. Save order with payment and shipping references
            saveOrderToDatabase(orderId, userId, orderDate, 
                              subtotal, taxAmount, deliveryFee, totalAmount, 
                              cartItems, paymentId, shippingId);
            
            // 4. Update inventory
            updateInventory(cartItems);
            
            // 5. Clear cart and set order confirmation attributes
            session.removeAttribute("cart");
            session.setAttribute("cartSize", 0);
            session.setAttribute("orderId", orderId);
            session.setAttribute("orderDate", orderDate);
            
            // 6. Redirect to thank you page
            response.sendRedirect(request.getContextPath() + "/JSP/ThankYou.jsp");
            
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Order confirmation failed: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/CartServlet");
        }
    }
    
    private String generateOrderId() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private void saveOrderToDatabase(String orderId, String userId, String orderDate, Double subtotal, Double taxAmount, Double deliveryFee, Double totalAmount, List<CartItem> cartItems, Integer paymentId, Integer shippingId) throws SQLException {
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/glowydays", "nbuser", "nbuser");
            
            // 1. Save orders
            String orderSql = "INSERT INTO NBUSER.ORDERS " +
                            "(\"orderId\", \"userId\", \"orderDate\", \"paymentId\", \"shippingId\", \"subtotal\", \"taxAmount\", \"deliveryFee\", \"totalAmount\") " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(orderSql);
            pstmt.setString(1, orderId);
            pstmt.setString(2, userId);
            pstmt.setString(3, orderDate);
            pstmt.setInt(4, paymentId);
            pstmt.setInt(5, shippingId);
            pstmt.setDouble(6, subtotal);
            pstmt.setDouble(7, taxAmount);
            pstmt.setDouble(8, deliveryFee);
            pstmt.setDouble(9, totalAmount);
            pstmt.executeUpdate();
            
            // 2. Save order items
            String itemSql = "INSERT INTO NBUSER.ORDERITEMS " +
                           "(\"orderId\", \"productId\", \"productName\", \"quantity\", \"unitPrice\", \"subtotal\") " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(itemSql);
            for (CartItem item : cartItems) {
                pstmt.setString(1, orderId);
                pstmt.setInt(2, item.getProduct().getId());
                pstmt.setString(3, item.getProduct().getName());
                pstmt.setInt(4, item.getQuantity());
                pstmt.setDouble(5, item.getProduct().getPrice());
                pstmt.setDouble(6, item.getSubtotal());
                pstmt.executeUpdate();
            }
            
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
    
    private void updateInventory(List<CartItem> cartItems) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/glowydays", "nbuser", "nbuser");
            String updateSql = "UPDATE NBUSER.PRODUCTS SET stock = stock - ? WHERE \"productId\" = ?";
            pstmt = conn.prepareStatement(updateSql);
            
            for (CartItem item : cartItems) {
                pstmt.setInt(1, item.getQuantity());
                pstmt.setInt(2, item.getProduct().getId());
                pstmt.executeUpdate();
            }
            
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        }
    }
}