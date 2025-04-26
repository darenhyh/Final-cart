<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="model.CartItem" %>
<%@page import="java.text.DecimalFormat" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout</title>
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
        
        .checkout-container {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            flex-wrap: wrap;
            gap: 40px;
        }
        
        .checkout-form {
            flex: 1;
            min-width: 300px;
        }
        
        .order-summary {
            width: 350px;
        }
        
        h1 {
            font-size: 28px;
            font-weight: bold;
            margin-bottom: 10px;
            text-align: center;
        }
        
        .checkout-total {
            text-align: center;
            margin-bottom: 40px;
            font-size: 16px;
        }
        
        .section {
            margin-bottom: 30px;
        }
        
        .section-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            font-size: 18px;
            font-weight: bold;
        }
        
        .edit-link {
            font-size: 14px;
            text-decoration: none;
            color: #000;
            font-weight: normal;
        }
        
        .form-group {
            margin-bottom: 15px;
            position: relative;
        }
        
        .form-label {
            display: block;
            margin-bottom: 5px;
            font-size: 14px;
        }
        
        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            background-color: #fff;
        }
        
        .form-control:focus {
            outline: none;
            border-color: #000;
        }
        
        .checkbox-label {
            display: flex;
            align-items: center;
            font-size: 14px;
            cursor: pointer;
            margin: 10px 0;
        }
        
        .checkbox-label input {
            margin-right: 10px;
        }
        
        .validation-icon {
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
            color: green;
        }
        
        .address-info {
            background-color: #f7f7f7;
            padding: 15px;
            font-size: 14px;
            margin-bottom: 20px;
        }
        
        .delivery-option {
            border: 1px solid #ccc;
            padding: 15px;
            margin-bottom: 15px;
            cursor: pointer;
        }
        
        .delivery-option.selected {
            border-color: #000;
        }
        
        .delivery-option-header {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }
        
        .delivery-details {
            color: #666;
            font-size: 14px;
        }
        
        .payment-option {
            border: 1px solid #ccc;
            padding: 15px;
            margin-bottom: 15px;
            cursor: pointer;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .payment-option:hover {
            border-color: #000;
        }
        
        .payment-logos {
            display: flex;
            gap: 5px;
        }
        
        .payment-logos img {
            height: 25px;
        }
        
        .security-note {
            background-color: #f7f7f7;
            padding: 15px;
            font-size: 14px;
            margin-bottom: 20px;
            display: flex;
            align-items: center;
        }
        
        .security-note i {
            margin-right: 10px;
        }
        
        .next-button {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            padding: 15px 20px;
            background-color: #000;
            color: white;
            border: none;
            font-weight: bold;
            cursor: pointer;
            margin-bottom: 25px;
            text-decoration: none;
        }
        
        .card-icons {
            display: flex;
            gap: 10px;
        }
        
        .card-icons img {
            height: 30px;
        }
        
        .terms {
            font-size: 14px;
            margin-top: 10px;
        }
        
        .terms a {
            color: #000;
        }
        
        .order-item {
            display: flex;
            margin-bottom: 20px;
        }
        
        .order-item-image {
            width: 80px;
            height: 80px;
            background-color: #f5f5f5;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-right: 15px;
        }
        
        .order-item-image img {
            max-width: 70px;
            max-height: 70px;
        }
        
        .order-item-details {
            flex: 1;
        }
        
        .order-item-name {
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        .order-item-price {
            font-weight: bold;
        }
        
        .order-item-info {
            font-size: 12px;
            color: #666;
        }
        
        .summary-item {
            display: flex;
            justify-content: space-between;
            margin-bottom: 15px;
            font-size: 14px;
        }
        
        .summary-total {
            display: flex;
            justify-content: space-between;
            margin-bottom: 5px;
            font-weight: bold;
        }
        
        .tax-info {
            font-size: 12px;
            color: #666;
            margin-bottom: 25px;
        }
        
        .promo-section {
            margin-bottom: 25px;
            display: flex;
            align-items: center;
        }
        
        .promo-btn {
            background-color: white;
            border: 1px solid #000;
            padding: 5px 10px;
            font-size: 12px;
            cursor: pointer;
            margin-right: 10px;
        }
        
        .promo-link {
            text-decoration: none;
            color: #000;
            font-size: 14px;
            font-weight: bold;
        }
        
        .divider {
            border-top: 1px solid #e0e0e0;
            margin: 20px 0;
        }
        
        /* Dropdown style */
        .dropdown {
            position: relative;
            width: 100%;
        }
        
        .dropdown select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            appearance: none;
            background-color: white;
        }
        
        .dropdown::after {
            content: "▼";
            font-size: 12px;
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(-50%);
            pointer-events: none;
        }
        
        /* Custom select style */
        .custom-select {
            position: relative;
        }
        
        .custom-select::after {
            content: "▼";
            position: absolute;
            right: 15px;
            top: 15px;
            pointer-events: none;
        }
    </style>
</head>
<body>
    <% 
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        // Get cart and pricing details from session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        Integer cartSize = (Integer) session.getAttribute("cartSize");
        Double subtotal = (Double) session.getAttribute("subtotal");
        Double taxAmount = (Double) session.getAttribute("taxAmount");
        Double deliveryFee = (Double) session.getAttribute("deliveryFee");
        Double totalAmount = (Double) session.getAttribute("totalAmount");
        
        // Default values if not in session
        if (subtotal == null) subtotal = 0.0;
        if (taxAmount == null) taxAmount = 0.0;
        if (deliveryFee == null) deliveryFee = 0.0;
        if (totalAmount == null) totalAmount = subtotal + taxAmount + deliveryFee;
        if (cartSize == null) cartSize = 0;
    %>
    
    <h1>CHECKOUT</h1>
    <div class="checkout-total">[<%= cartSize %> items] RM<%= df.format(totalAmount) %></div>
    
    <div class="checkout-container">
        <div class="checkout-form">
            <!-- Contact Section -->
            <div class="section">
                <div class="section-header">
                    <span>CONTACT</span>
                    <a href="#" class="edit-link">EDIT</a>
                </div>
                <div class="form-group">
                    <label class="form-label">Email *</label>
                    <div style="position: relative;">
                        <input type="email" class="form-control" value="huyh-wm23@student.tarc.edu.my" required>
                        <span class="validation-icon">✓</span>
                    </div>
                </div>
            </div>
            
            <!-- Address Section -->
            <div class="section">
                <div class="section-header">
                    <span>ADDRESS</span>
                    <a href="#" class="edit-link">EDIT</a>
                </div>
                
                <div>
                    <h3>Delivery address</h3>
                    <div class="form-group">
                        <label class="form-label">First Name *</label>
                        <div style="position: relative;">
                            <input type="text" class="form-control" value="dwa" required>
                            <span class="validation-icon">✓</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Last Name *</label>
                        <div style="position: relative;">
                            <input type="text" class="form-control" value="dwa" required>
                            <span class="validation-icon">✓</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Address 1 *</label>
                        <div style="position: relative;">
                            <input type="text" class="form-control" value="dwad" required>
                            <span class="validation-icon">✓</span>
                        </div>
                        <div class="form-hint">Street name and number</div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Address 2 *</label>
                        <div style="position: relative;">
                            <input type="text" class="form-control" value="wa" required>
                            <span class="validation-icon">✓</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Additional Info</label>
                        <div style="position: relative;">
                            <input type="text" class="form-control" value="dwa">
                            <span class="validation-icon">✓</span>
                        </div>
                        <div class="form-hint">Additional address information</div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">State</label>
                        <div class="custom-select">
                            <select class="form-control">
                                <option>KEDAH</option>
                                <option>KUALA LUMPUR</option>
                                <option>SELANGOR</option>
                                <option>JOHOR</option>
                                <option>PENANG</option>
                            </select>
                        </div>
                    </div>
                    <div style="display: flex; gap: 15px;">
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">City</label>
                            <div class="custom-select">
                                <select class="form-control">
                                    <option>ALOR SETAR</option>
                                    <option>KUALA KEDAH</option>
                                    <option>SUNGAI PETANI</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group" style="flex: 1;">
                            <label class="form-label">Postal Code</label>
                            <div class="custom-select">
                                <select class="form-control">
                                    <option>05050</option>
                                    <option>05100</option>
                                    <option>05150</option>
                                </select>
                            </div>
                            <div class="form-hint">Example: 50000</div>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Country</label>
                        <div style="margin-bottom: 10px;">Malaysia</div>
                    </div>
                    <div class="form-group">
                        <label class="form-label">Phone Number *</label>
                        <div style="position: relative;">
                            <input type="tel" class="form-control" value="" required>
                            <span class="validation-icon">✓</span>
                        </div>
                    </div>
                    <div class="form-hint">We will only call you if there are questions regarding your order.</div>
                    <div class="checkbox-label">
                        <input type="checkbox" id="sameAddress" required>
                        <label for="sameAddress">My billing and delivery information are the same.</label>
                    </div>
                    <div class="checkbox-label">
                        <input type="checkbox" id="ageCheck" required>
                        <label for="ageCheck">I'm 16+ years old.</label>
                    </div>
                </div>
            </div>
            
            <!-- Delivery Options Section -->
            <div class="section">
                <div class="section-header">
                    <span>DELIVERY OPTIONS</span>
                    <a href="#" class="edit-link">EDIT</a>
                </div>
                
                <div class="delivery-option selected">
                    <div class="delivery-option-header">
                        <span><strong>By Thursday 09 May</strong></span>
                        <span>🚚</span>
                    </div>
                    <div class="delivery-details">
                        <div><strong>Free</strong></div>
                        <div>Ninjavan - Standard delivery</div>
                        <div>09:00 - 17:00</div>
                    </div>
                </div>
                
                <div>Or collect your order at a pickup point instead:</div>
                
                <div class="delivery-option">
                    <div class="delivery-option-header">
                        <span><strong>Click & Collect</strong></span>
                        <span>🏬</span>
                    </div>
                    <div class="delivery-details">
                        <div><strong>Free</strong></div>
                        <div>Pay now, collect in store.</div>
                    </div>
                </div>
            </div>
            
            <!-- Payment Section -->
            <div class="section">
                <div class="section-header">
                    <span>PAYMENT</span>
                </div>
                
                <div class="security-note">
                    <span>🔒</span>
                    <span>Payments are SSL encrypted so that your credit card and payment details stay safe.</span>
                </div>
                
                <div class="payment-option">
                    <span>Credit/Debit Card</span>
                    <div class="card-icons">
                        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/visa/visa-original.svg" alt="Visa">
                        <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mastercard/mastercard-original.svg" alt="Mastercard">
                    </div>
                </div>
                
                <div class="payment-option">
                    <span>Paypal</span>
                    <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/paypal/paypal-original.svg" alt="PayPal" style="height: 25px;">
                </div>
                
                <div class="payment-option">
                    <span>MY Online Banking</span>
                    <div style="height: 25px; display: flex; gap: 5px;">
                        <div style="background-color: #FFD700; width: 40px; height: 25px;"></div>
                        <div style="background-color: #FF6347; width: 40px; height: 25px;"></div>
                        <div style="background-color: #4682B4; width: 40px; height: 25px;"></div>
                    </div>
                </div>
                
                <button class="next-button" type="submit">
                    <span>PLACE ORDER</span>
                    <span>→</span>
                </button>
                
                <div class="terms">
                    By clicking Place Order you agree to the <a href="#">Terms & Conditions</a>.
                </div>
            </div>
        </div>
        
        <!-- Order Summary Section -->
        <div class="order-summary">
            <div class="section-header">
                <span>YOUR CART</span>
                <a href="<%= request.getContextPath() %>/CartServlet" class="edit-link">EDIT</a>
            </div>
            
            <div class="summary-item">
                <span><%= cartSize %> items</span>
                <span>RM<%= df.format(subtotal) %></span>
            </div>
            <div class="summary-item">
                <span>Delivery</span>
                <% if (deliveryFee > 0) { %>
                    <span>RM<%= df.format(deliveryFee) %></span>
                <% } else { %>
                    <span>Free</span>
                <% } %>
            </div>
            <div class="summary-total">
                <span>Total</span>
                <span>RM<%= df.format(totalAmount) %></span>
            </div>
            <div class="tax-info">[Inclusive of tax RM<%= df.format(taxAmount) %>]</div>
            
            <div class="promo-section">
                <button class="promo-btn">+</button>
                <a href="#" class="promo-link">USE A PROMO CODE</a>
            </div>
            
            <div class="divider"></div>
            
            <% if(cart != null && !cart.isEmpty()) { 
                for(CartItem item : cart) { %>
                <div class="order-item">
                    <div class="order-item-image">
                        <img src="<%= request.getContextPath() %>/ProductImages/<%= item.getProduct().getImageUrl() %>" alt="<%= item.getProduct().getName() %>">
                    </div>
                    <div class="order-item-details">
                        <div class="order-item-name"><%= item.getProduct().getName() %></div>
                        <div class="order-item-price">RM<%= df.format(item.getProduct().getPrice()) %></div>
                        <div class="order-item-info">
                            <% if(item.getProduct().getCategory() != null) { %>
                                Size: <%= (int)(Math.random() * 12) + 4 %> UK / Quantity: <%= item.getQuantity() %><br>
                                Colour: <%= item.getProduct().getCategory() %> / <%= (Math.random() > 0.5 ? "Grey" : "Black") %> Six
                            <% } else { %>
                                Size: <%= (int)(Math.random() * 12) + 4 %> UK / Quantity: <%= item.getQuantity() %>
                            <% } %>
                        </div>
                    </div>
                </div>
            <% } 
            } %>
        </div>
    </div>
</body>
</html>