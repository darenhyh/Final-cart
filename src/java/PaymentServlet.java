import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.CartItem;

@WebServlet("/PaymentServlet")
public class PaymentServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        // Check if cart exists and is not empty
        if (cart == null || cart.isEmpty()) {
            // Redirect back to cart with a message
            response.sendRedirect(request.getContextPath() + "/CartServlet");
            return;
        }
        
        // Calculate subtotal
        double subtotal = 0.0;
        for (CartItem item : cart) {
            subtotal += item.getSubtotal();
        }
        
        // Calculate tax (16%)
        double taxRate = 0.16;
        double taxAmount = subtotal * taxRate;
        
        // Calculate delivery fee
        double deliveryFee = 0.0;
        if (subtotal < 1000) {
            deliveryFee = 50.0; // RM50 delivery fee
        }
        
        // Calculate total
        double totalAmount = subtotal + taxAmount + deliveryFee;
        
        // Store all values in session for use in JSP
        session.setAttribute("subtotal", subtotal);
        session.setAttribute("taxAmount", taxAmount);
        session.setAttribute("deliveryFee", deliveryFee);
        session.setAttribute("totalAmount", totalAmount);
        
        // Forward to payment shipping page
        request.getRequestDispatcher("/JSP/PaymentShipping.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle payment processing
        // For demonstration purposes, we're just redirecting to the order confirmation
        
        // In a real application, you would:
        // 1. Validate payment information
        // 2. Process the payment through a payment gateway
        // 3. Create an order record in the database
        // 4. Update inventory
        // 5. Clear the cart
        // 6. Send confirmation email
        
        // Clear cart after successful order
        HttpSession session = request.getSession();
        session.removeAttribute("cart");
        session.setAttribute("cartSize", 0);
        
        // Set an order confirmation message
        session.setAttribute("orderConfirmationMessage", "Your order has been placed successfully!");
        
        // Redirect to order confirmation page
        response.sendRedirect(request.getContextPath() + "/JSP/OrderConfirmation.jsp");
    }
}