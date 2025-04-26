<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="model.Product" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product</title>
        <style>
            * {
                box-sizing: border-box;
                margin: 0;
                padding: 0;
            }

            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
            }

            .outer-container {
                width: 100%;
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
            }

            .inner-container {
                position: relative;
                padding-top: 60px;
            }

            .cart-container {
                position: absolute;
                top: 10px;
                right: 10px;
                z-index: 100;
            }
            
            .cart-badge {
                position: absolute;
                top: -10px;
                right: -10px;
                background-color: red;
                color: white;
                border-radius: 50%;
                width: 20px;
                height: 20px;
                display: flex;
                justify-content: center;
                align-items: center;
                font-size: 12px;
                font-weight: bold;
            }

            .products {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                gap: 20px;
            }

            .product-item {
                background-color: white;
                border-radius: 8px;
                padding: 15px;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
                transition: transform 0.3s ease;
            }

            .product-item:hover {
                transform: translateY(-5px);
            }

            .product-image {
                width: 100%;
                height: 200px;
                object-fit: cover;
                border-radius: 4px;
            }

            figure {
                margin-bottom: 15px;
            }

            figcaption {
                text-align: center;
                margin-top: 5px;
                font-size: 14px;
                color: #666;
            }

            h2 {
                font-size: 18px;
                margin-bottom: 10px;
                color: #333;
            }

            .price {
                font-weight: bold;
                color: #e44d26;
                margin-bottom: 10px;
            }

            p {
                margin-bottom: 15px;
                color: #666;
                font-size: 14px;
            }

            button {
                background-color: #4CAF50;
                color: white;
                border: none;
                padding: 10px 15px;
                border-radius: 4px;
                cursor: pointer;
                width: 100%;
                font-size: 14px;
                transition: background-color 0.3s ease;
            }

            button:hover {
                background-color: #45a049;
            }
            
            .product-added-message {
                position: fixed;
                top: 20px;
                right: 20px;
                background-color: #4CAF50;
                color: white;
                padding: 15px;
                border-radius: 5px;
                display: none;
                z-index: 1000;
            }
        </style>
    </head>
    <body> 
        <div id="productAddedMessage" class="product-added-message">
            Product added to cart!
        </div>
        
        <div class="outer-container">
            <div class="inner-container">
                <div class="cart-container">
                    <a href="<%= request.getContextPath() %>/CartServlet">
                        <img src="<%= request.getContextPath() %>/ICON/cart.svg" alt="Cart" width="45" height="45">
                        <% 
                            Integer cartSize = (Integer) session.getAttribute("cartSize");
                            if (cartSize != null && cartSize > 0) {
                        %>
                            <span class="cart-badge"><%= cartSize %></span>
                        <% } %>
                    </a>
                </div>
               
                <section class="products">
                    <% 
                        List<Product> products = (List<Product>) request.getAttribute("products");
                        if (products != null && !products.isEmpty()) {
                            for (Product p : products) {
                    %>
                        <article class="product-item">
                            <form action="<%= request.getContextPath() %>/CartServlet" method="POST" class="add-to-cart-form">
                                <figure>
                                    <img class="product-image" src="<%= request.getContextPath() %>/ProductImages/<%= p.getImageUrl() %>" alt="<%= p.getName() %>">
                                    <figcaption><%= p.getName() %></figcaption>
                                </figure>
                                <h2><%= p.getName() %></h2>
                                <p class="price">RM<%= p.getPrice() %></p>
                                <p><%= p.getDescription() %></p>
                                <input type="hidden" name="PRODUCT_ID" value="<%= p.getId() %>" /> 
                                <input type="hidden" name="PRODUCTNAME" value="<%= p.getName() %>" />
                                <input type="hidden" name="PRICE" value="<%= p.getPrice() %>" />
                                <input type="hidden" name="IMAGE_URL" value="<%= p.getImageUrl() %>" />
                                <button type="submit" class="add-to-cart-btn">Add to cart</button>
                            </form>
                        </article>
                    <% 
                            }
                        } else {
                    %>
                        <p>No products available.</p>
                    <% } %>
                </section>
            </div>         
        </div>
        
        <script>
            // Show notification when a product is added to cart
            document.addEventListener('DOMContentLoaded', function() {
                const forms = document.querySelectorAll('.add-to-cart-form');
                const message = document.getElementById('productAddedMessage');
                
                forms.forEach(form => {
                    form.addEventListener('submit', function(e) {
                        // Submit the form normally
                        
                        // Show the message
                        message.style.display = 'block';
                        
                        // Hide the message after 3 seconds
                        setTimeout(function() {
                            message.style.display = 'none';
                        }, 3000);
                    });
                });
            });
        </script>
    </body>
</html>