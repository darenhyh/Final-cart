package listener;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionAttributeListener;
import jakarta.servlet.http.HttpSessionBindingEvent;
import java.util.List;
import model.CartItem;
import dao.CartDAO;

@WebListener
public class LoginListener implements HttpSessionAttributeListener {

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        // Check if userId was just added to session (user just logged in)
        if ("userId".equals(event.getName())) {
            HttpSession session = event.getSession();
            Integer userId = (Integer) event.getValue();
            
            if (userId != null) {
                // Load cart from database
                CartDAO cartDAO = new CartDAO();
                List<CartItem> dbCart = cartDAO.getCartItems(userId);
                
                // Set cart in session
                session.setAttribute("cart", dbCart);
                
                // Calculate total items in cart
                int totalItems = 0;
                for (CartItem item : dbCart) {
                    totalItems += item.getQuantity();
                }
                session.setAttribute("cartSize", totalItems);
            }
        }
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        // Not needed for this implementation
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        // Not needed for this implementation
    }
}