import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/CompleteCheckoutServlet")
public class CompleteCheckoutServlet extends HttpServlet {
    
    private static final String JDBC_URL = "jdbc:derby://localhost:1527/product";
    private static final String USERNAME = "user";
    private static final String PASSWORD = "pass";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Integer pendingCartId = (Integer) session.getAttribute("pendingCartId");
        
        if (pendingCartId == null) {
            // If no pending cart found, redirect to home
            response.sendRedirect(request.getContextPath() + "/ProductServlet");
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            
            // Update cart status to COMPLETED
            String updateCartQuery = "UPDATE APP.Cart SET Status = 'COMPLETED', LastUpdated = CURRENT_TIMESTAMP WHERE CartID = ?";
            stmt = conn.prepareStatement(updateCartQuery);
            stmt.setInt(1, pendingCartId);
            stmt.executeUpdate();
            
            // Clear the cart in the session
            session.removeAttribute("cart");
            session.setAttribute("cartSize", 0);
            session.removeAttribute("pendingCartId");
            
            // Set order confirmation details
            session.setAttribute("orderConfirmed", true);
            session.setAttribute("orderNumber", pendingCartId);
            
            // Redirect to order confirmation page
            response.sendRedirect(request.getContextPath() + "/JSP/OrderConfirmation.jsp");
            
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred during checkout completion: " + e.getMessage());
            request.getRequestDispatcher("/JSP/PaymentShippingForm.jsp").forward(request, response);
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}