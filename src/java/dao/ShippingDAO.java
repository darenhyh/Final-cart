package dao;

import model.Address;
import model.BuyerDetail;
import model.ShippingDetail;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShippingDAO {
    
    private Connection getConnection() throws Exception {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        return DriverManager.getConnection("jdbc:derby://localhost:1527/glowydays", "nbuser", "nbuser");
    }

    public boolean saveShipping(BuyerDetail buyer, Address address) {
        Connection con = null;
        PreparedStatement buyerStmt = null;
        PreparedStatement addressStmt = null;
        PreparedStatement shipStmt = null;
        boolean success = false;
        
        try {
            // Get connection
            con = getConnection();
            
            // Start transaction
            con.setAutoCommit(false);

            // 1. Insert BuyerDetail
            String buyerSql = "INSERT INTO NBUSER.BUYERDETAIL (\"fullName\", \"email\", \"mobile\") VALUES (?, ?, ?)";
            buyerStmt = con.prepareStatement(buyerSql, Statement.RETURN_GENERATED_KEYS);
            buyerStmt.setString(1, buyer.getFullName());
            buyerStmt.setString(2, buyer.getEmail());
            buyerStmt.setString(3, buyer.getMobile());
            buyerStmt.executeUpdate(); 

            int buyerId = 0;
            ResultSet buyerKeys = buyerStmt.getGeneratedKeys();
            if (buyerKeys.next()) {
                buyerId = buyerKeys.getInt(1); // get generated buyerId
            } else {
                throw new SQLException("Creating buyer failed, no ID obtained.");
            }
            
            // 2. Insert Address
            String addressSql = "INSERT INTO NBUSER.ADDRESS (\"address\", \"city\", \"state\", \"postcode\") VALUES (?, ?, ?, ?)";
            addressStmt = con.prepareStatement(addressSql, Statement.RETURN_GENERATED_KEYS);
            addressStmt.setString(1, address.getAddress());
            addressStmt.setString(2, address.getCity());
            addressStmt.setString(3, address.getState());
            addressStmt.setString(4, address.getPostcode());
            addressStmt.executeUpdate();

            int addressId = 0;
            ResultSet addressKeys = addressStmt.getGeneratedKeys();
            if (addressKeys.next()) {
                addressId = addressKeys.getInt(1); // get generated addressId
            } else {
                throw new SQLException("Creating address failed, no ID obtained.");
            }

            // 3. Insert ShippingDetail (linking buyerId & addressId)
            String shipSql = "INSERT INTO NBUSER.SHIPPINGDETAIL(\"buyerId\", \"addressId\") VALUES (?, ?)";
            shipStmt = con.prepareStatement(shipSql);
            shipStmt.setInt(1, buyerId);
            shipStmt.setInt(2, addressId);
            shipStmt.executeUpdate();

            // Commit transaction
            con.commit();
            success = true;
            
            } catch (Exception e) {
                // Rollback transaction on error
                try {
                    if (con != null) {
                        con.rollback();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                e.printStackTrace();
            } finally {
                // Close resources
                try {
                    if (shipStmt != null) shipStmt.close();
                    if (addressStmt != null) addressStmt.close();
                    if (buyerStmt != null) buyerStmt.close();
                    if (con != null) con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
            }
        }
        return success;
    }
    
    public int getShippingIdByEmailAndMobile(String email, String mobile) throws Exception {
        int shippingId = 0;

        try (Connection conn = getConnection()) {
            // Derby-compatible query to get the most recent shippingId
            String sql = "SELECT sd.\"shippingId\" FROM NBUSER.SHIPPINGDETAIL sd " +
                        "JOIN NBUSER.BUYERDETAIL bd ON sd.\"buyerId\" = bd.\"buyerId\" " +
                        "WHERE bd.\"email\" = ? AND bd.\"mobile\" = ? " +
                        "ORDER BY sd.\"shippingId\" DESC";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, mobile);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    shippingId = rs.getInt("shippingId");
                }
            }
        }
    
        return shippingId;
    }
}

// FOR TESTING PURPOSE
//package dao;
//
//import model.Address;
//import model.BuyerDetail;
//import model.ShippingDetail;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ShippingDAO {
//    
//    private Connection getConnection() throws Exception {
//        Class.forName("org.apache.derby.jdbc.ClientDriver");
//        return DriverManager.getConnection("jdbc:derby://localhost:1527/glowydays", "nbuser", "nbuser");
//    }
//
//    public int getShippingIdByEmailAndMobile(String email, String mobile) throws Exception {
//        int shippingId = 0;
//
//        try (Connection conn = getConnection()) {
//            // Derby-compatible query to get the most recent shippingId
//            String sql = "SELECT sd.\"shippingId\" FROM NBUSER.SHIPPINGDETAIL sd " +
//                        "JOIN NBUSER.BUYERDETAIL bd ON sd.\"buyerId\" = bd.\"buyerId\" " +
//                        "WHERE bd.\"email\" = ? AND bd.\"mobile\" = ? " +
//                        "ORDER BY sd.\"shippingId\" DESC";
//
//            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
//                stmt.setString(1, email);
//                stmt.setString(2, mobile);
//                ResultSet rs = stmt.executeQuery();
//                if (rs.next()) {
//                    shippingId = rs.getInt("shippingId");
//                }
//            }
//        }
//    
//        return shippingId;
//    }
//
//    public static void main(String[] args) {
//        ShippingDAO dao = new ShippingDAO();
//
//        try {
//            // 1. First get a real email/mobile pair from your database
//            Connection conn = dao.getConnection();
//            String getTestDataSql = "SELECT bd.\"email\", bd.\"mobile\", MAX(sd.\"shippingId\") as latestId " +
//                                  "FROM NBUSER.SHIPPINGDETAIL sd " +
//                                  "JOIN NBUSER.BUYERDETAIL bd ON sd.\"buyerId\" = bd.\"buyerId\" " +
//                                  "GROUP BY bd.\"email\", bd.\"mobile\" " +
//                                  "ORDER BY latestId DESC " +
//                                  "FETCH FIRST 1 ROW ONLY";
//
//            String testEmail = null;
//            String testMobile = null;
//            int expectedShippingId = 0;
//
//            try (PreparedStatement stmt = conn.prepareStatement(getTestDataSql);
//                 ResultSet rs = stmt.executeQuery()) {
//
//                if (rs.next()) {
//                    testEmail = rs.getString("email");
//                    testMobile = rs.getString("mobile");
//                    expectedShippingId = rs.getInt("latestId");
//                    System.out.println("Testing with most recent record:");
//                    System.out.println("Email: " + testEmail);
//                    System.out.println("Mobile: " + testMobile);
//                    System.out.println("Expected shippingId: " + expectedShippingId);
//                } else {
//                    System.out.println("No shipping records found in database!");
//                    return;
//                }
//            }
//
//            // 2. Test the method
//            System.out.println("\nCalling getShippingIdByEmailAndMobile()...");
//            int actualShippingId = dao.getShippingIdByEmailAndMobile(testEmail, testMobile);
//
//            // 3. Verify results
//            System.out.println("\nTest Results:");
//            System.out.println("Expected shippingId: " + expectedShippingId);
//            System.out.println("Actual shippingId: " + actualShippingId);
//
//            if (actualShippingId == expectedShippingId) {
//                System.out.println("✅ SUCCESS: Retrieved correct shippingId");
//            } else if (actualShippingId == 0) {
//                System.out.println("❌ FAILURE: No shippingId found");
//            } else {
//                System.out.println("❌ FAILURE: Retrieved wrong shippingId");
//            }
//
//        } catch (Exception e) {
//            System.err.println("Error during testing:");
//            e.printStackTrace();
//        }
//    }
//}