import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.Product;
import model.User;
import dao.CartDAO;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        // If user is logged in, get their cart from database
        User user = (User) session.getAttribute("user");
        if (user != null && (cart == null || cart.isEmpty())) {
            CartDAO cartDAO = new CartDAO();
            cart = cartDAO.getCartItems(user.getId());
            session.setAttribute("cart", cart);
            
            // Calculate total items in cart
            int totalItems = 0;
            for (CartItem item : cart) {
                totalItems += item.getQuantity();
            }
            session.setAttribute("cartSize", totalItems);
        }
        
        // Forward to cart JSP page
        request.getRequestDispatcher("/JSP/Cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get cart from session or create a new one if it doesn't exist
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        
        // Get product information from request parameters
        int productId = Integer.parseInt(request.getParameter("PRODUCT_ID"));
        String productName = request.getParameter("PRODUCTNAME");
        double productPrice = Double.parseDouble(request.getParameter("PRICE"));
        String imageUrl = request.getParameter("IMAGE_URL");
        
        // Create Product object
        Product product = new Product();
        product.setId(productId);
        product.setName(productName);
        product.setPrice(productPrice);
        product.setImageUrl(imageUrl);
        
        // Check if product already exists in cart
        boolean productExists = false;
        for (CartItem item : cart) {
            if (item.getProduct().getId() == productId) {
                // Increment quantity
                item.setQuantity(item.getQuantity() + 1);
                productExists = true;
                break;
            }
        }
        
        // If product doesn't exist in cart, add new item
        if (!productExists) {
            CartItem newItem = new CartItem(product, 1);
            cart.add(newItem);
        }
        
        // Calculate total items in cart
        int totalItems = 0;
        for (CartItem item : cart) {
            totalItems += item.getQuantity();
        }
        session.setAttribute("cartSize", totalItems);
        
        // Save to database if user is logged in
        User user = (User) session.getAttribute("user");
        if (user != null) {
            CartDAO cartDAO = new CartDAO();
            // Find the item we just added or updated
            for (CartItem item : cart) {
                if (item.getProduct().getId() == productId) {
                    cartDAO.addCartItem(user.getId(), item);
                    break;
                }
            }
        }
        
        // Redirect back to product page or to cart page based on request
        String referer = request.getHeader("Referer");
        if (referer != null && referer.contains("ProductServlet")) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/CartServlet");
        }
    }
}