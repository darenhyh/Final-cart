package dao;

import java.sql.*;

public class GenerateCartIDDAO {

    public static int generateCartID(int userID) {
        int cartID = -1; // Default value if something goes wrong

        try {
            // Connect to the database
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/product", "user", "pass");

            // Insert a new cart for the user
            String sql = "INSERT INTO APP.Cart (UserID) VALUES (?)";
            PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, userID);  // Use the UserID passed as a parameter

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                // Get the auto-generated CartID
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    cartID = rs.getInt(1);  // Get the generated CartID as an integer
                }
                rs.close();
            }

            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartID;
    }

}
