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
        return DriverManager.getConnection("jdbc:derby://localhost:1527/glowydays", "nbuser", "nbuser");
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
            String methodSql = "INSERT INTO NBUSER.PAYMENTMETHOD(\"methodName\", \"cardOwner\", \"cardNumber\", \"expMonth\", \"expYear\", \"cvv\") VALUES (?, ?, ?, ?, ?, ?)";
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
            String paySql = "INSERT INTO NBUSER.PAYMENT(\"methodId\", \"paidDate\", \"paidTime\") VALUES (?, ?, ?)";
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
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return success;
    }
    
    // ✅ 新增方法：通过 cardNumber 查询最新的 paymentId
    public int getPaymentIdByCardNumber(String cardNumber) throws Exception {
        int paymentId = 0;

        try (Connection conn = getConnection()) {
            String sql = "SELECT paymentId FROM NBUSER.PAYMENT " +
                         "WHERE methodId = (SELECT MAX(methodId) FROM NBUSER.PAYMENTMETHOD WHERE cardNumber = ?) " +
                         "ORDER BY paymentId DESC FETCH FIRST ROW ONLY";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, cardNumber);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    paymentId = rs.getInt("paymentId");
                }
            }
        }

        return paymentId;
    }
}

// FOR TESTING PURPOSE
//    public List<PaymentMethod> getAllPaymentMethods() {
//        List<PaymentMethod> methods = new ArrayList<>();
//        Connection con = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = getConnection();
//            String sql = "SELECT * FROM NBUSER.PAYMENTMETHOD";
//            stmt = con.prepareStatement(sql);
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//                PaymentMethod method = new PaymentMethod();
//                method.setMethodId(rs.getInt("methodId"));
//                method.setMethodName(rs.getString("methodName"));
//                method.setCardOwner(rs.getString("cardOwner"));
//                method.setCardNumber(rs.getString("cardNumber"));
//                method.setExpMonth(rs.getString("expMonth"));
//                method.setExpYear(rs.getString("expYear"));
//                method.setCvv(rs.getString("cvv"));
//                methods.add(method);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) rs.close();
//                if (stmt != null) stmt.close();
//                if (con != null) con.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return methods;
//    }
//
//    public static void main(String[] args) {
//        PaymentDAO dao = new PaymentDAO();
//
//        // Test: Retrieve and print all payment methods
//        List<PaymentMethod> methodList = dao.getAllPaymentMethods();
//        if (methodList.isEmpty()) {
//            System.out.println("No payment methods found.");
//        } else {
//            for (PaymentMethod method : methodList) {
//                System.out.println("Method ID: " + method.getMethodId());
//                System.out.println("Method Name: " + method.getMethodName());
//                System.out.println("Card Owner: " + method.getCardOwner());
//                System.out.println("Card Number: " + method.getCardNumber());
//                System.out.println("Expiry: " + method.getExpMonth() + "/" + method.getExpYear());
//                System.out.println("CVV: " + method.getCvv());
//                System.out.println("---------------------------");
//            }
//        }
//    }