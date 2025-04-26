import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import model.CartItem;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
    
    // Regular expression for email validation
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    
    // Regular expression for phone validation (Malaysian format: starts with 0, 10-11 digits)
    private static final String PHONE_REGEX = "^0\\d{9,10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    
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
        session.setAttribute("validEmail", false); // Initially set email validation to false
        
        // Forward to payment shipping page
        request.getRequestDispatcher("/JSP/PaymentShipping.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Get form data
        String email = request.getParameter("email");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String address1 = request.getParameter("address1");
        String address2 = request.getParameter("address2");
        String additionalInfo = request.getParameter("additionalInfo");
        String state = request.getParameter("state");
        String city = request.getParameter("city");
        String postalCode = request.getParameter("postalCode");
        String phoneNumber = request.getParameter("phoneNumber");
        String deliveryMethod = request.getParameter("deliveryMethod");
        String paymentMethod = request.getParameter("paymentMethod");
        
        // Validate email
        boolean isValidEmail = validateEmail(email);
        session.setAttribute("validEmail", isValidEmail);
        
        // Validate phone number
        boolean isValidPhone = validatePhone(phoneNumber);
        session.setAttribute("validPhone", isValidPhone);
        
        // If email is invalid, redirect back to checkout page with error
        if (!isValidEmail) {
            request.setAttribute("emailError", "Please check your email address is correct.");
            request.getRequestDispatcher("/JSP/PaymentShipping.jsp").forward(request, response);
            return;
        }
        
        // If phone is invalid, redirect back with error
        if (!isValidPhone) {
            request.setAttribute("phoneError", "Please enter a valid Malaysian phone number (starts with 0, 10-11 digits).");
            request.getRequestDispatcher("/JSP/PaymentShipping.jsp").forward(request, response);
            return;
        }
        
        // Proceed only if both email and phone are valid
        if (isValidEmail && isValidPhone) {
            // Store checkout information in session
            session.setAttribute("checkoutEmail", email);
            session.setAttribute("checkoutFirstName", firstName);
            session.setAttribute("checkoutLastName", lastName);
            session.setAttribute("checkoutAddress1", address1);
            session.setAttribute("checkoutAddress2", address2);
            session.setAttribute("checkoutAdditionalInfo", additionalInfo);
            session.setAttribute("checkoutState", state);
            session.setAttribute("checkoutCity", city);
            session.setAttribute("checkoutPostalCode", postalCode);
            session.setAttribute("checkoutPhoneNumber", phoneNumber);
            session.setAttribute("checkoutDeliveryMethod", deliveryMethod);
            session.setAttribute("checkoutPaymentMethod", paymentMethod);
            
            // For this example, we'll just redirect to a confirmation page
            response.sendRedirect(request.getContextPath() + "/JSP/OrderConfirmation.jsp");
        }
    }
    
    /**
     * Validates email address format
     * @param email The email to validate
     * @return True if valid, false otherwise
     */
    private boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    
    /**
     * Validates Malaysian phone number format
     * @param phone The phone number to validate
     * @return True if valid, false otherwise
     */
    private boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
}