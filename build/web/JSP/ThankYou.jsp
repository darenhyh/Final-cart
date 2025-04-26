<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Payment, model.BuyerDetail" %>

<!--<!DOCTYPE html>
<html>
<head>
    <title>Thank You</title>
    <style>
        .thank-you { text-align: center; margin-top: 50px; }
        .receipt { margin: 20px auto; padding: 20px; border: 1px solid #ddd; width: 60%; }
        a { color: #4CAF50; }
    </style>
</head>
<body>
    <div class="thank-you">
        <div class="receipt">
        <h1>Thank You for Your Order!</h1>
            <p>We've received your order and are processing it. You'll receive a confirmation email shortly.</p>
            <p>Your order ID: <strong>${payment.orderId}</strong></p>
            <p>A receipt has been sent to: <strong>${buyer.email}</strong></p>
            <p>Your order ID: ${orderId}</p>
            <p>A receipt has been sent to your email.</p> 
            
            <div class="order-details">
                <p>Estimated delivery: 3-5 business days</p>
            </div>
            
            <p><a href="UserHome.jsp">Return to Home</a></p>
        </div>
    </div>
</body>
</html>-->



<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thank You for Your Order</title>
    <link href="../CSS/payment.css" rel="stylesheet" type="text/css">
</head>
<body>
    <div class="container">
        <div class="thank-you-box">
            <h1 class="title">Order Received</h1>
            
            <div class="order-summary">
                <p class="summary-text">Thank you. Your order has been received.</p>
                
                <div class="summary-grid">
                    <div class="summary-item">
                        <span class="summary-label">Order Number</span>
                        <span class="summary-value">${payment.transactionId}</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">Order Date</span>
                        <span class="summary-value">${orderDate}</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">Order Status</span>
                        <span class="summary-value">Processing</span>
                    </div>
                    <div class="summary-item">
                        <span class="summary-label">Payment Method</span>
                        <span class="summary-value">${paymentMethod}</span>
                    </div>
                </div>
            </div>
            
            <div class="receipt-details">
                <h2 class="subtitle">Order Details</h2>
                <p>A confirmation email has been sent to: <strong>${buyer.email}</strong></p>
                
                 You can add order items table here if needed 
                 Example:
                <table class="order-items">
                    <tr>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Price</th>
                    </tr>
                    <c:forEach items="${orderItems}" var="item">
                        <tr>
                            <td>${item.productName}</td>
                            <td>${item.quantity}</td>
                            <td>RM ${item.price}</td>
                        </tr>
                    </c:forEach>
                </table>
                
                
                <div class="delivery-info">
                    <p><strong>Estimated delivery:</strong> 3-5 business days</p>
                </div>
            </div>
            
            <div class="action-buttons">
                <a href="UserHome.jsp" class="home-link">Return to Home</a>
                <a href="OrderHistory.jsp" class="order-history-link">View Order History</a>
            </div>
        </div>
    </div>
</body>
</html>