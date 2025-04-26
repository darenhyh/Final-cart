import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.CartItem;
import model.User;
import dao.CartDAO;

@WebServlet("/UpdateCartServlet")
public class UpdateCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart != null) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            int cartItemId = -1;
            
            for (CartItem item : cart) {
                if (item.getProduct().getId() == productId) {
                    item.setQuantity(quantity);
                    cartItemId = item.getId();
                    break;
                }
            }
            
            // Check if user is logged in and update in database
            User user = (User) session.getAttribute("user");
            if (user != null && cartItemId != -1) {
                CartDAO cartDAO = new CartDAO();
                cartDAO.updateCartItem(cartItemId, quantity);
            }
            
            // Recalculate total items in cart
            int totalItems = 0;
            for (CartItem item : cart) {
                totalItems += item.getQuantity();
            }
            session.setAttribute("cartSize", totalItems);
        }
        
        response.sendRedirect(request.getContextPath() + "/CartServlet");
    }
}