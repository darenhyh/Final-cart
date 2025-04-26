import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); // false means don't create a new session
        if (session != null) {
            // Keep cart items in session, just remove user attributes
            session.removeAttribute("user");
            session.removeAttribute("role");
            // Don't invalidate the entire session as that would lose the cart
        }
        // Redirect to GuestHome.jsp
        response.sendRedirect("/JSP/GuestHome.jsp");
    }
}