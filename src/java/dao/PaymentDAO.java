package dao;

import model.Payment;
import model.PaymentMethod;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    private Connection getConnection() throws Exception {
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        return DriverManager.getConnection("jdbc:derby://localhost:1527/product", "u", "u");
    }

    public boolean savePayment(PaymentMethod method) {
        Connection con = null;
        PreparedStatement methodStmt = null;
        PreparedStatement payStmt = null;
        boolean success = false;
        
        try {
            // Get connection
            con = getConnection();
            
            // Start transaction
            con.setAutoCommit(false);
                        
            // 1. Insert PaymentMethod
            String methodSql = "INSERT INTO APP.PAYMENTMETHOD(\"methodName\", \"cardOwner\", \"cardNumber\", \"expMonth\", \"expYear\", \"cvv\") VALUES (?, ?, ?, ?, ?, ?)";
            methodStmt = con.prepareStatement(methodSql, Statement.RETURN_GENERATED_KEYS);
            methodStmt.setString(1, method.getMethodName());
            methodStmt.setString(2, method.getCardOwner());
            methodStmt.setString(3, method.getCardNumber());
            methodStmt.setString(4, method.getExpMonth());
            methodStmt.setString(5, method.getExpYear());
            methodStmt.setString(6, method.getCvv());
            methodStmt.executeUpdate();
            
            ResultSet methodKeys = methodStmt.getGeneratedKeys();
            int methodId = 0;
            if (methodKeys.next()) {
                methodId = methodKeys.getInt(1);
            } else {
                throw new SQLException("Creating payment method failed, no ID obtained.");
            }

            // 2. Insert payment
            String paySql = "INSERT INTO APP.PAYMENT(\"methodId\", \"paidDate\", \"paidTime\") VALUES (?, ?, ?)";
            payStmt = con.prepareStatement(paySql);
            payStmt.setInt(1, methodId);
            payStmt.setDate(2, Date.valueOf(LocalDate.now()));
            payStmt.setTime(3, Time.valueOf(LocalTime.now()));
            payStmt.executeUpdate();

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
                if (payStmt != null) payStmt.close();
                if (methodStmt != null) methodStmt.close();
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
    
    public int getPaymentIdByCardNumber(String cardNumber) {
        int paymentId = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT p.\"paymentId\" FROM APP.PAYMENT p " +
                         "JOIN APP.PAYMENTMETHOD pm ON p.\"methodId\" = pm.\"methodId\" " +
                         "WHERE pm.\"cardNumber\" = ? " +
                         "ORDER BY p.\"paymentId\" DESC FETCH FIRST ROW ONLY";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, cardNumber);
            rs = stmt.executeQuery();
            if (rs.next()) {
                paymentId = rs.getInt("paymentId");
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

        return paymentId;
    }
    
    // New method to get the latest payment ID by method name
    public int getLatestPaymentIdByMethod(String methodName) {
        int paymentId = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection();
            String sql = "SELECT p.\"paymentId\" FROM APP.PAYMENT p " +
                         "JOIN APP.PAYMENTMETHOD pm ON p.\"methodId\" = pm.\"methodId\" " +
                         "WHERE pm.\"methodName\" = ? " +
                         "ORDER BY p.\"paymentId\" DESC FETCH FIRST ROW ONLY";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, methodName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                paymentId = rs.getInt("paymentId");
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

        return paymentId;
    }
}