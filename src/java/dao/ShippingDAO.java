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
        return DriverManager.getConnection("jdbc:derby://localhost:1527/product", "u", "u");
    }

    public boolean saveShipping(BuyerDetail buyer, Address address) {
        Connection con = null;
        PreparedStatement buyerStmt = null;
        PreparedStatement addressStmt = null;
        PreparedStatement shipStmt = null;
        boolean success = false;
        
        try {
            // Validate inputs before database operations
            if (buyer == null || address == null || 
                buyer.getFullName() == null || buyer.getEmail() == null || buyer.getMobile() == null ||
                address.getAddress() == null || address.getCity() == null || 
                address.getState() == null || address.getPostcode() == null) {
                return false;
            }
            
            // Get connection
            con = getConnection();
            
            // Start transaction
            con.setAutoCommit(false);

            // 1. Insert BuyerDetail
            String buyerSql = "INSERT INTO APP.BUYERDETAIL (\"fullName\", \"email\", \"mobile\") VALUES (?, ?, ?)";
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
            String addressSql = "INSERT INTO APP.ADDRESS (\"address\", \"city\", \"state\", \"postcode\") VALUES (?, ?, ?, ?)";
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
            String shipSql = "INSERT INTO APP.SHIPPINGDETAIL(\"buyerId\", \"addressId\") VALUES (?, ?)";
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
                if (con != null) {
                    con.setAutoCommit(true); // Reset auto-commit mode
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return success;
    }
    
    public int getShippingIdByEmailAndMobile(String email, String mobile) {
        int shippingId = 0;
        Connection conn = null; 
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Validate inputs
            if (email == null || mobile == null || email.trim().isEmpty() || mobile.trim().isEmpty()) {
                return 0;
            }
            
            conn = getConnection();
            // Derby-compatible query to get the most recent shippingId
            String sql = "SELECT sd.\"shippingId\" FROM APP.SHIPPINGDETAIL sd " +
                        "JOIN APP.BUYERDETAIL bd ON sd.\"buyerId\" = bd.\"buyerId\" " +
                        "WHERE bd.\"email\" = ? AND bd.\"mobile\" = ? " +
                        "ORDER BY sd.\"shippingId\" DESC FETCH FIRST ROW ONLY";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, mobile);
            rs = stmt.executeQuery();
            if (rs.next()) {
                shippingId = rs.getInt("shippingId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return shippingId;
    }
}