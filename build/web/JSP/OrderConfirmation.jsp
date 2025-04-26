<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="model.CartItem" %>
<%@page import="java.text.DecimalFormat" %>
<%@page import="java.util.Date" %>
<%@page import="java.text.SimpleDateFormat" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Arial', sans-serif;
        }
        
        body {
            padding: 40px;
            color: #000;
            background-color: #fff;
        }
        
        .container {
            max-width: 800px;
            margin: 0 auto;
        }
        
        .success-header {
            text-align: center;
            margin-bottom: 40px;
        }
        
        .success-icon {
            font-size: 50px;
            color: #4CAF50;
            margin-bottom: 20px;
        }
        
        h1 {
            font-size: 28px;
            font-weight: bold;
            margin-bottom: 15px;
        }
        
        .order-message {
            font-size: 16px;
            margin-bottom: 10px;
            text-align: center;
        }
        
        .order-number {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 30px;
            text-align: center;
        }
        
        .order-details {
            border: 1px solid #e0e0e0;
            padding: 25px;
            margin-bottom: 30px;
        }
        
        .section-title {
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 15px;
        }
        
        .detail-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            font-size: 14px;
        }
        
        .detail-label {
            font-weight: bold;
            margin-right: 20px;
        }
        
        .divider {
            border-top: 1px solid #e0e0e0;
            margin: 20px 0;
        }
        
        .items-container {
            margin-bottom: 30px;
        }
        
        .order-item {
            display: flex;
            margin-bottom: 20px;
        }
        
        .item-image {
            width: 80px;
            height: 80px;
            background-color: #f5f5f5;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
        }
        
        .item-image img {
            max-width: 70px;
            max-height: 70px;
        }
        
        .item-details {
            flex: 1;
        }
        
        .item-name {
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        .item-price {
            font-weight: bold;
        }
        
        .item-info {
            font-size: 12px;
            color: #666;
        }
        
        .summary {
            background-color: #f7f7f7;
            padding: 20px;
            margin-bottom: 30px;
        }
        
        .summary-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
            font-size: 14px;
        }
        
        .summary-total {
            display: flex;
            justify-content: space-between;
            font-weight: bold;
            font-size: 16px;
        }
        
        .tax-info {
            font-size: 12px;
            color: #666;
            text-align: right;
            margin-top: 5px;
        }
        
        .actions {
            display: flex;
            justify-content: space-between;
            margin-top: 40px;
        }
        
        .button {
            display: inline-block;
            padding: 15px 25px;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
            text-align: center;
        }
        
        .primary-button {
            background-color: #000;
            color: white;
            border: none;
        }
        
        .secondary-button {
            background-color: #fff;
            color: #000;
            border: 1px solid #000;
        }
        
        .shipping-address, .payment-info {
            margin-bottom: 30px;
        }
        
        .address-details, .payment-details {
            font-size: 14px;
            line-height: 1.6;
        }
        
        .estimated-delivery {
            background-color: #f7f7f7;
            padding: 15px;
            margin: 20px 0;
            font-size: 14px;
        }
        
        .delivery-date {
            font-weight: bold;
        }
        
        .delivery-method {
            margin-top: 5px;
            color: #666;
        }
        
        .contact-support {
            text-align: center;
            margin-top: 40px;
            font-size: 14px;
        }
        
        .contact-support a {
            color: #000;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <% 
        // Get order confirmation message
        String orderConfirmationMessage = (String) session.getAttribute("orderConfirmationMessage");
        if (orderConfirmationMessage == null) {
            orderConfirmationMessage = "Your order has been placed successfully!";
        }
        
        // Generate a random order number
        String orderNumber = "MY" + (int)(Math.random() * 900000 + 100000);
        
        // Get the current date for order date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        String orderDate = dateFormat.format(new Date());
        
        // Calculate estimated delivery date (current date + 7 days)
        Date deliveryDate = new Date();
        deliveryDate.setTime(deliveryDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        String estimatedDelivery = dateFormat.format(deliveryDate);
        
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        // Get total amounts from session or use placeholder values
        Double subtotal = (Double) session.getAttribute("subtotal");
        Double taxAmount = (Double) session.getAttribute("taxAmount");
        Double deliveryFee = (Double) session.getAttribute("deliveryFee");
        Double totalAmount = (Double) session.getAttribute("totalAmount");
        
        // Default values if not in session
        if (subtotal == null) subtotal = 2135.00;
        if (taxAmount == null) taxAmount = 0.00;
        if (deliveryFee == null) deliveryFee = 0.0;
        if (totalAmount == null) totalAmount = 2135.00;
    %>
    
    <div class="container">
        <div class="success-header">
            <div class="success-icon">✓</div>
            <h1>Thank You For Your Order!</h1>
            <p class="order-message"><%= orderConfirmationMessage %></p>
            <p class="order-number">Order #<%= orderNumber %></p>
        </div>
        
        <div class="order-details">
            <div class="section-title">Order Details</div>
            <div class="detail-row">
                <span class="detail-label">Order Date:</span>
                <span><%= orderDate %></span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Order Number:</span>
                <span><%= orderNumber %></span>
            </div>
            <div class="detail-row">
                <span class="detail-label">Payment Method:</span>
                <span>Credit Card</span>
            </div>
            <div class="divider"></div>
            
            <div class="estimated-delivery">
                <div class="delivery-date">Estimated Delivery: <%= estimatedDelivery %></div>
                <div class="delivery-method">Ninjavan - Standard delivery (09:00 - 17:00)</div>
            </div>
            
            <div class="shipping-address">
                <div class="section-title">Shipping Address</div>
                <div class="address-details">
                    dwa dwa<br>
                    dwad<br>
                    wa<br>
                    dwa<br>
                    ALOR SETAR, KEDAH, 05050, MY<br>
                    01110111166
                </div>
            </div>
            
            <div class="payment-info">
                <div class="section-title">Payment Information</div>
                <div class="payment-details">
                    Credit Card<br>
                    **** **** **** 1234<br>
                    Expires: 05/2025
                </div>
            </div>
        </div>
        
        <div class="items-container">
            <div class="section-title">Order Items</div>
            
            <!-- Item 1 -->
            <div class="order-item">
                <div class="item-image">
                    <img src="${pageContext.request.contextPath}/ProductImages/adilette_slides_grey.jpg" alt="Adilette Slides">
                </div>
                <div class="item-details">
                    <div class="item-name">Adilette 25 Slides</div>
                    <div class="item-price">RM369</div>
                    <div class="item-info">
                        Size: 11 UK / Quantity: 1<br>
                        Colour: Grey Two / Grey Two / Solar Orange
                    </div>
                </div>
            </div>
            
            <!-- Item 2 -->
            <div class="order-item">
                <div class="item-image">
                    <img src="${pageContext.request.contextPath}/ProductImages/adilette_slides_black.jpg" alt="Adilette Slides">
                </div>
                <div class="item-details">
                    <div class="item-name">Adilette 25 Slides</div>
                    <div class="item-price">RM369</div>
                    <div class="item-info">
                        Size: 6 UK / Quantity: 1<br>
                        Colour: Core Black / Core Black / Grey Six
                    </div>
                </div>
            </div>
            
            <!-- Item 3 -->
            <div class="order-item">
                <div class="item-image">
                    <img src="${pageContext.request.contextPath}/ProductImages/adilette_platform_clogs.jpg" alt="Adilette Platform Clogs">
                </div>
                <div class="item-details">
                    <div class="item-name">Adilette Platform Clogs</div>
                    <div class="item-price">RM199</div>
                    <div class="item-info">
                        Size: 7 UK / Quantity: 1<br>
                        Colour: Beige / Putty Mauve / Putty Mauve
                    </div>
                </div>
            </div>
            
            <!-- Item 4 -->
            <div class="order-item">
                <div class="item-image">
                    <img src="${pageContext.request.contextPath}/ProductImages/adizero_shoes_black.jpg" alt="Adizero Shoes">
                </div>
                <div class="item-details">
                    <div class="item-name">Adizero Aruku Shoes</div>
                    <div class="item-price">RM599</div>
                    <div class="item-info">
                        Size: 4.5 UK / Quantity: 1<br>
                        Colour: Core Black / Carbon / Iron Metallic
                    </div>
                </div>
            </div>
            
            <!-- Item 5 -->
            <div class="order-item">
                <div class="item-image">
                    <img src="${pageContext.request.contextPath}/ProductImages/adizero_shoes_blue.jpg" alt="Adizero Shoes">
                </div>
                <div class="item-details">
                    <div class="item-name">Adizero Aruku Shoes</div>
                    <div class="item-price">RM599</div>
                    <div class="item-info">
                        Size: 12.5 UK / Quantity: 1<br>
                        Colour: Lucid Blue / Lucid Lemon / Grey Two
                    </div>
                </div>
            </div>
        </div>
        
        <div class="summary">
            <div class="summary-row">
                <span>Subtotal</span>
                <span>RM<%= df.format(subtotal) %></span>
            </div>
            <div class="summary-row">
                <span>Shipping</span>
                <span>Free</span>
            </div>
            <div class="divider"></div>
            <div class="summary-total">
                <span>Total</span>
                <span>RM<%= df.format(totalAmount) %></span>
            </div>
            <div class="tax-info">[Inclusive of tax RM<%= df.format(taxAmount) %>]</div>
        </div>
        
        <div class="actions">
            <a href="${pageContext.request.contextPath}/ProductServlet" class="button secondary-button">CONTINUE SHOPPING</a>
            <a href="#" class="button primary-button">TRACK ORDER</a>
        </div>
        
        <div class="contact-support">
            Questions about your order? <a href="#">Contact our customer support</a>
        </div>
    </div>
</body>
</html>