<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Payment Shipping Form</title>
    <link href="../CSS/payment.css" rel="stylesheet" type="text/css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <div class="container">
        <form action="/GlowyDays-master/PaymentShippingServlet" method="post">
            <div class="row">
                <div class="column">
                    <h3 class="title">Shipping Address</h3>
                    <div class="input-box">
                        <label>Full Name</label>
                        <input type="text" name="shippingName" id="shippingName" placeholder="Enter full name" required>
                        <div id="shipNameValidation"></div>
                    </div>
                    <div class="input-box">
                        <label>Email</label>
                        <input type="email" name="shippingEmail" id="shippingEmail" placeholder="Enter email" required>
                        <div id="shipEmailValidation"></div>
                    </div>
                    <div class="input-box">
                        <label>Mobile Number</label>
                        <input type="tel" name="shippingMobile" id="shippingMobile" pattern="01[0-9]-[0-9]{7,10}" placeholder="Enter mobile number (01X-XXXXXXX)" required>
                        <div id="shipMobileValidation"></div>
                    </div>
                    <div class="input-box">
                        <label>Address</label>
                        <input type="text" name="shippingAddress" id="shippingAddress" placeholder="Enter address" required>
                        <div id="shipAddressValidation"></div>
                    </div>
                    <div class="input-box">
                        <label>City</label>
                        <input type="text" name="shippingCity" id="shippingCity" placeholder="Enter city" required>
                        <div id="shipCityValidation"></div>
                    </div>

                    <div class="flex">
                        <div class="input-box">
                            <label>State</label>
                            <select name="shippingState" id="shippingState" required>
                                <option value="">Choose state</option>
                                <option value="Johor">Johor</option>
                                <option value="Kedah">Kedah</option>
                                <option value="Kelantan">Kelantan</option>
                                <option value="KualaLumpur">Kuala Lumpur</option>
                                <option value="Labuan">Labuan</option>
                                <option value="Melaka">Melaka</option>
                                <option value="NegeriSembilan">Negeri Sembilan</option>
                                <option value="Pahang">Pahang</option>
                                <option value="Penang">Penang</option>
                                <option value="Perak">Perak</option>
                                <option value="Perlis">Perlis</option>
                                <option value="Putrajaya">Putrajaya</option>
                                <option value="Sabah">Sabah</option>
                                <option value="Sarawak">Sarawak</option>
                                <option value="Selangor">Selangor</option>
                                <option value="Terengganu">Terengganu</option>
                            </select>
                            <div id="shipStateValidation"></div>
                        </div>
                        <div class="input-box">
                            <label>Postcode</label>
                            <input type="number" name="shippingPostcode" id="shippingPostcode" maxlength="5" placeholder="Enter postcode" required> 
                            <div id="shipPostcodeValidation"></div>
                        </div>
                    </div>
                </div>

                <div class="column">
                    <h3 class="title">Payment</h3>
                    <div class="input-box">
                        <label>Payment Method</label>
                        <input type="hidden" name="payment_method" id="payment_method">
                        <div id="paymtMethodValidation"></div>
                        <div class="icon-container">
                            <button type="button" id="cashIcon" class="cashIcon" data-method="cash"><img src="ICON/cash.svg"></button>
                            <button type="button" id="tngIcon" class="tngIcon" data-method="tng"><img src="ICON/tng.svg"></button>
                            <button type="button" id="visaIcon" class="visaIcon" data-method="visa"><img src="ICON/visa.svg"></button>
                            <button type="button" id="mcIcon" class="mcIcon" data-method="master"><img src="ICON/mastercard.svg"></button>
                        </div>
                    </div>
                    
                    <div class="input-box">
                        <label>Name On Card</label>
                        <input type="text" name="cardOwner" id="cardOwner" placeholder="Enter card owner" disabled>
                        <div id="cardOwnerValidation"></div>
                    </div>
                    <div class="input-box">
                        <label>Credit Card Number</label>
                        <input type="text" name="cardNumber" id="cardNumber" maxlength="16" pattern="\d{15,16}" placeholder="Enter card number" disabled>
                        <div id="cardNumValidation"></div>
                    </div>
                    <div class="input-box">
                        <label>Exp Month</label>
                        <input type="number" name="expMonth" id="expMonth" maxlength="2" min="1" max="12" placeholder="Enter expiry month (MM)" disabled>
                        <div id="expMonthValidation"></div>
                    </div>

                    <div class="input-box">
                        <label>Exp Year </label>
                        <input type="number" name="expYear" id="expYear" maxlength="4" min="2025" max="2100" placeholder="Enter exp year (YYYY)" disabled>
                        <div id="expYearValidation"></div>
                    </div>
                    <div class="input-box">
                        <label>CVV</label>
                        <input type="number" name="cvv" id="cvv" maxlength="3" placeholder="Enter CVV with 3 digits" disabled> 
                        <div id="cvvValidation"></div>
                    </div>
                </div>
            </div>            
            <button type="submit" class="submitBtn">Pay Now</button>
        </form>
    </div>
    
    <!-- VALIDATE SHIPPING DETAIL -->
<!-- Replace the problematic shipping validation section with this fixed code -->
<script>
    $(document).ready(function() {
        // -------------------------------
        //    SHIPPING NAME VALIDATION
        // -------------------------------
        $('#shippingName').on('keyup', function() {
            var name = $(this).val().trim();
            var nameRegex = /^[A-Za-z\s/]+$/; // Only letters, spaces, and '/'

            // Only check if there's input      
            if (name.length > 0) { 
                if (!nameRegex.test(name)) {
                    $('#shipNameValidation').html('<span style="color:red; font-size:13px;">Invalid characters detected! Only alphabets, spaces, and "/" are allowed.</span>');
                    $('.submitBtn[type="submit"]').prop('disabled', true);
                } else {
                    $.ajax({
                        type: 'POST',
                        url: '/GlowyDays-JDBC/ValidateShippingServlet', // Calls the servlet
                        data: { shippingName: name }, // Match parameter name with servlet
                        success: function (response) {
                            if (response.trim() === "Valid Name") {
                                $('#shipNameValidation').html('<span style="color:green; font-size:13px;">Valid Name!</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', false);
                            } else {
                                $('#shipNameValidation').html('<span style="color:red; font-size:13px;">Invalid Name.</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', true);
                            }
                        },
                        error: function () {
                            $('#shipNameValidation').html('<span style="color:red;">Error validating name.</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', true);
                        }
                    });
                }
            } else {
                $('#shipNameValidation').html(''); // Clear validation message
                $('.submitBtn[type="submit"]').prop('disabled', true); // Ensure form is disabled
            }
        });
    
        // -------------------------------
        //    SHIPPING EMAIL VALIDATION
        // -------------------------------
        $('#shippingEmail').on('keyup', function() {
            var email = $(this).val().trim(); // Trim whitespace
            var emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Regular expression for valid email format

            // Only check if there's input      
            if (email.length > 0) { 
                if (!emailRegex.test(email)) {
                    $('#shipEmailValidation').html('<span style="color:red; font-size:13px;">Invalid email format! Please enter a valid email address.</span>');
                    $('.submitBtn[type="submit"]').prop('disabled', true);
                } else {
                    $.ajax({
                        type: 'POST',
                        url: '/GlowyDays-JDBC/ValidateShippingServlet', // Calls the servlet
                        data: { shippingEmail: email }, // Match parameter name with servlet
                        success: function (response) {
                            if (response.trim() === "Valid Email") {
                                $('#shipEmailValidation').html('<span style="color:green; font-size:13px;">Valid email!</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', false);
                            } else {
                                $('#shipEmailValidation').html('<span style="color:red; font-size:13px;">Invalid email.</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', true);
                            }
                        },
                        error: function () {
                            $('#shipEmailValidation').html('<span style="color:red;">Error validating email.</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', true);
                        }
                    });
                }
            } else {
                $('#shipEmailValidation').html(''); // Clear validation message
                $('.submitBtn[type="submit"]').prop('disabled', true); // Ensure form is disabled
            }
        });

        // -------------------------------
        //   SHIPPING MOBILE VALIDATION
        // -------------------------------
        $('#shippingMobile').on('keyup', function() {
            var mobile = $(this).val().trim(); // Trim whitespace
            var mobileRegex = /^01[0-9]-[0-9]{7,8}$/; // Regular expression for valid mobile format

            // Only check if there's input      
            if (mobile.length > 0) { 
                if (!mobileRegex.test(mobile)) {
                    $('#shipMobileValidation').html('<span style="color:red; font-size:13px;">Invalid mobile format! Please enter a valid mobile number.</span>');
                    $('.submitBtn[type="submit"]').prop('disabled', true);
                } else {
                    $.ajax({
                        type: 'POST',
                        url: '/GlowyDays-JDBC/ValidateShippingServlet', // Calls the servlet
                        data: { shippingMobile: mobile }, // Match parameter name with servlet
                        success: function (response) {
                            if (response.trim() === "Valid Mobile") {
                                $('#shipMobileValidation').html('<span style="color:green; font-size:13px;">Valid mobile!</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', false);
                            } else {
                                $('#shipMobileValidation').html('<span style="color:red; font-size:13px;">Invalid mobile.</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', true);
                            }
                        },
                        error: function () {
                            $('#shipMobileValidation').html('<span style="color:red;">Error validating mobile.</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', true);
                        }
                    });
                }
            } else {
                $('#shipMobileValidation').html(''); // Clear validation message
                $('.submitBtn[type="submit"]').prop('disabled', true); // Ensure form is disabled
            }
        });
        
        // -------------------------------
        //   SHIPPING ADDRESS VALIDATION
        // -------------------------------
        $('#shippingAddress').on('keyup', function() {
            var address = $(this).val().trim();
            
            // Only check if there's input 
            if (address.length > 0) {
                $.ajax({
                    type: 'POST',
                    url: '/GlowyDays-JDBC/ValidateShippingServlet', // Calls the servlet
                    data: { shippingAddress: address }, // Match parameter name with servlet
                    success: function(response) {
                        if (response.trim() === "Valid Address") {
                            $('#shipAddressValidation').html('<span style="color:green; font-size:13px;">Valid Address!</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', false);
                        } else {
                            $('#shipAddressValidation').html('<span style="color:red; font-size:13px;">Please enter your address.</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', true);
                        }
                    },
                    error: function() {
                        $('#shipAddressValidation').html('<span style="color:red;">Error validating address.</span>');
                        $('.submitBtn[type="submit"]').prop('disabled', true);
                    }
                });
            } else {
                $('#shipAddressValidation').html(''); // Clear validation message
                $('.submitBtn[type="submit"]').prop('disabled', true); // Disable submit button if input is empty
            }
        });
        
        // -------------------------------
        //     SHIPPING CITY VALIDATION
        // -------------------------------
        $('#shippingCity').on('keyup', function() {
            var city = $(this).val().trim();
            if (city.length > 0) {
                $.ajax({
                    type: 'POST',
                    url: '/GlowyDays-JDBC/ValidateShippingServlet',
                    data: { shippingCity: city }, // Match parameter name with servlet
                    success: function(response) {
                        if (response.trim() === "Valid City") {
                            $('#shipCityValidation').html('<span style="color:green; font-size:13px;">Valid City!</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', false);
                        } else {
                            $('#shipCityValidation').html('<span style="color:red; font-size:13px;">Please enter your city.</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', true);
                        }
                    },
                    error: function() {
                        $('#shipCityValidation').html('<span style="color:red;">Error validating city.</span>');
                        $('.submitBtn[type="submit"]').prop('disabled', true);
                    }
                });
            } else {
                $('#shipCityValidation').html('');
                $('.submitBtn[type="submit"]').prop('disabled', true);
            }
        });
        
        // -------------------------------
        //    SHIPPING STATE VALIDATION
        // -------------------------------
        $('#shippingState').on('change', function() {
            var state = $(this).val();
            
            if (state.length > 0) {
                $.ajax({
                    type: 'POST',
                    url: '/GlowyDays-JDBC/ValidateShippingServlet',
                    data: { shippingState: state }, // Match parameter name with servlet
                    success: function(response) {
                        if (response.trim() === "Valid State") {
                            $('#shipStateValidation').html('<span style="color:green; font-size:13px;">Valid State!</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', false);
                        } else {
                            $('#shipStateValidation').html('<span style="color:red; font-size:13px;">Please select a state.</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', true);
                        }
                    },
                    error: function() {
                        $('#shipStateValidation').html('<span style="color:red;">Error validating state.</span>');
                        $('.submitBtn[type="submit"]').prop('disabled', true);
                    }
                });
            } else {
                $('#shipStateValidation').html('<span style="color:red; font-size:13px;">Please select your state.</span>');
                $('.submitBtn[type="submit"]').prop('disabled', true);
            }
        });
        
        // -------------------------------
        //  SHIPPING POSTCODE VALIDATION
        // -------------------------------
        $('#shippingPostcode').on('keyup', function() {
            var postcode = $(this).val().trim();
            var postcodeRegex = /^\d{5}$/;
            
            if (postcode.length > 0) {
                if (postcodeRegex.test(postcode)) {  // Check if input matches "12345" format
                    $.ajax({
                        type: 'POST',
                        url: '/GlowyDays-JDBC/ValidateShippingServlet',
                        data: { shippingPostcode: postcode }, // Match parameter name with servlet
                        success: function(response) {
                            if (response.trim() === "Valid Postcode") {
                                $('#shipPostcodeValidation').html('<span style="color:green; font-size:13px;">Valid Postcode!</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', false);
                            } else {
                                $('#shipPostcodeValidation').html('<span style="color:red; font-size:13px;">Postcode not recognized.</span>');
                                $('.submitBtn[type="submit"]').prop('disabled', true);
                            }
                        },
                        error: function() {
                            $('#shipPostcodeValidation').html('<span style="color:red;">Server error. Try again.</span>');
                            $('.submitBtn[type="submit"]').prop('disabled', true);
                        }
                    });
                } else {
                    // Show error if not 5 digits
                    $('#shipPostcodeValidation').html('<span style="color:red; font-size:13px;">Postcode must be 5 digits (e.g., 12345).</span>');
                    $('.submitBtn[type="submit"]').prop('disabled', true);
                }
            } else {
                // Empty field
                $('#shipPostcodeValidation').html('');
                $('.submitBtn[type="submit"]').prop('disabled', true);
            }
        });
    });
</script>

<!-- Now add the payment validation script -->
<script>
    function validateForm() {
    // Check if a payment method is selected
    var paymentMethod = document.getElementById('payment_method').value;
    if (!paymentMethod) {
        alert('Please select a payment method');
        return false;
    }
    
    // For all payment methods, validate shipping details
    var shippingFields = [
        'shippingName', 'shippingEmail', 'shippingMobile', 
        'shippingAddress', 'shippingCity', 'shippingState', 'shippingPostcode'
    ];
    
    for (var i = 0; i < shippingFields.length; i++) {
        var field = document.getElementById(shippingFields[i]);
        if (!field.value.trim()) {
            alert('Please complete all shipping information');
            field.focus();
            return false;
        }
    }
    
    // For card payments, validate card details
    if (paymentMethod === 'visa' || paymentMethod === 'master') {
        var cardFields = [
            'cardOwner', 'cardNumber', 'expMonth', 'expYear', 'cvv'
        ];
        
        for (var j = 0; j < cardFields.length; j++) {
            var cardField = document.getElementById(cardFields[j]);
            if (!cardField.value.trim()) {
                alert('Please complete all card information');
                cardField.focus();
                return false;
            }
        }
        
        // Additional card validation
        var cardNumber = document.getElementById('cardNumber').value;
        if (cardNumber.length < 15 || cardNumber.length > 16) {
            alert('Card number must be 15-16 digits');
            document.getElementById('cardNumber').focus();
            return false;
        }
        
        var expMonth = parseInt(document.getElementById('expMonth').value);
        if (isNaN(expMonth) || expMonth < 1 || expMonth > 12) {
            alert('Expiry month must be between 1 and 12');
            document.getElementById('expMonth').focus();
            return false;
        }
        
        var expYear = parseInt(document.getElementById('expYear').value);
        var currentYear = new Date().getFullYear();
        if (isNaN(expYear) || expYear < currentYear) {
            alert('Expiry year must be current year or later');
            document.getElementById('expYear').focus();
            return false;
        }
        
        var cvv = document.getElementById('cvv').value;
        if (cvv.length !== 3 || isNaN(parseInt(cvv))) {
            alert('CVV must be a 3-digit number');
            document.getElementById('cvv').focus();
            return false;
        }
    }
    
    return true;
}

// Add the event listener to the form
document.addEventListener('DOMContentLoaded', function() {
    var form = document.querySelector('form');
    form.addEventListener('submit', function(e) {
        if (!validateForm()) {
            e.preventDefault();
        }
    });
});
</script>
    
    <!-- Payment Method Selection & Card Input -->
    <script>
        document.addEventListener('DOMContentLoaded', function(){
            const paymentMethodInput = document.getElementById("payment_method");
            const cardInputs = [
                document.getElementById("cardOwner"),
                document.getElementById("cardNumber"),
                document.getElementById("expYear"),
                document.getElementById("cvv"),
                document.getElementById("expMonth")
            ];
            
            const paymentButtons = {
                cash: document.getElementById("cashIcon"),
                tng: document.getElementById("tngIcon"),
                visa: document.getElementById("visaIcon"),
                master: document.getElementById("mcIcon")
            };

            // Highlight selected button and remove highlight from others
            function highlightSelected(selectedBtn) {
                Object.values(paymentButtons).forEach(btn => btn.classList.remove("selected"));
                selectedBtn.classList.add("selected");
            }
            
            // Enable/disable card inputs & manage required attribute
            function setPaymentMethod(method) {
                paymentMethodInput.value = method;
                const enable = method === "visa" || method === "master";
                
                // Loop 
                cardInputs.forEach(input => {
                    input.disabled = !enable; // Enable/disable the field
                    input.style.backgroundColor = enable ? "white" : "#f0f0f0"; // Set background color
                    if (!enable) input.value = ""; // Clear if disable
                    input.required = enable; // Toggle required attribute
                });
            }

            // Add click event to each payment method button
            Object.entries(paymentButtons).forEach(([method, btn]) => {
                btn.addEventListener("click", () => {
                    setPaymentMethod(method);      // Enable/disable fields based on method
                    highlightSelected(btn);        // Visually indicate selected meth
                });
            });
        });
    </script>
</body>
</html>