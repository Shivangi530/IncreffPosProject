function getSignupUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/session/signup";
}
function getLoginUrl() {
    var baseUrl = $("meta[name=baseUrl]").attr("content")
    return baseUrl + "/site/login";
}

function signupUser(event) {
    var $form = $("#signupForm");
    var formData = $form.serialize();
    var json = toJson($form);
    var url = getSignupUrl();
    console.log(url);
    var json1 = JSON.parse(json);
    if (json1.email == "") {
        warning("Email cannot be empty");
        return false;
    }
    if (json1.password == "") {
        warning("Password cannot be empty");
        return false;
    }
    if(!isValidPassword(json1.password)){
        return false;
    }
   
    $.ajax({
        url: url,
        type: 'POST',
        data: formData,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        success: function(response) {
            success("Success");
            window.location.href = getLoginUrl();
            resetFormFields($form);
        },
        error: handleAjaxError
    });
    return false;
}

function isValidPassword(password) {
    const minLength = 8;
    const maxLength = 12;
    const specialCharacters = "!@#\\$%^&*()\\-_\\=+\\[\\]{}\\|;:,.<>?";

    if (password.length < minLength) {
        console.log(`Password must be at least ${minLength} characters long`);
        warning(`Password must be at least ${minLength} characters long.`);
        return false;
    }
    if (password.length > maxLength) {
        console.log(`Password cannot be more than ${maxLength} characters long`);
        warning(`Password cannot be more than ${maxLength} characters long.`);
        return false;
    }

    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasDigit = /\d/.test(password);
    const hasSpecialChar = new RegExp(`[${specialCharacters}]`).test(password);

    if (!hasUppercase) {
        warning("Password must contain at least one uppercase letter.");
        return false;
    }

    if (!hasLowercase) {
        warning("Password must contain at least one lowercase letter.");
        return false;
    }

    if (!hasDigit) {
        warning("Password must contain at least one digit.");
        return false;
    }

    if (!hasSpecialChar) {
        warning(`Password must contain at least one special character (${specialCharacters}).`);
        return false;
    }

    return true;
}

function init() {
    $('#signup').click(signupUser);
}

$(document).ready(init);




//document.addEventListener("DOMContentLoaded", function() {
//    var signupForm = document.getElementById("signupForm");
//    signupForm.addEventListener("submit", function(event) {
//        event.preventDefault(); // Prevent form submission
//        console.log("in function");
//        var email = document.getElementById("email").value;
//        var password = document.getElementById("password").value;
//        console.log(email, password);
//        // Password validation
//        try {
//            isValidPassword(password);
//        } catch (error) {
//            console.log("invalid password")
//            warning("Invalid password: " + error.message);
//            return; // Stop form submission if the password is invalid
//        }
//
//        // Create an object to store form data
//        var formData = {
//            email: email,
//            password: password
//        };
//        console.log("formData= ", formData);
//        // Send a POST request to the signup API endpoint
//        fetch("/pos/session/signup", {
//                method: "POST",
//                headers: {
//                    "Content-Type": "application/json"
//                },
//                body: JSON.stringify(formData)
//            })
//            .then(function(response) {
//                if (response.ok) {
//                    // Redirect to the login page on successful signup
//                    window.location.href = "/site/login";
//                } else {
//                    // Display error message
//                    alert("Error occurred during user registration");
//                }
//            })
//            .catch(function(error) {
//                console.error("Error:", error);
//                // Display error message
//                alert("Error occurred during user registration");
//            });
//    });
//});

//document.addEventListener("DOMContentLoaded", function() {
//    var signupForm = document.getElementById("signupForm");
//    signupForm.addEventListener("submit", function(event) {
//        event.preventDefault(); // Prevent form submission
//
//        var email = document.getElementById("email").value;
//        var password = document.getElementById("password").value;
//
//        // Password validation
//        try {
//            isValidPassword(password);
//        } catch (error) {
//            alert("Invalid password: " + error.message);
//            return; // Stop form submission if the password is invalid
//        }
//
//        // Create an object to store form data
//        var formData = {
//            email: email,
//            password: password
//        };
//        console.log("formData= ", formData);
//        // Send a POST request to the signup API endpoint
//        fetch("/session/signup", {
//            method: "POST",
//            headers: {
//                "Content-Type": "application/json"
//            },
//            body: JSON.stringify(formData)
//        })
//        .then(function(response) {
//            if (response.ok) {
//                // Redirect to the login page on successful signup
//                window.location.href = "/site/login";
//            } else {
//                // Handle server error response
//                console.error("Server responded with status: ", response.status);
//
//                return response.json();
//            }
//        })
//        .then(function(data) {
//            console.log("Response data: ", data);
//            alert("Error occurred during user registration. Please check console for details.");
//
////            if (data && data.error) {
////                alert("Error occurred during user registration: " + data.error);
////            } else {
////                alert("Unknown error occurred during user registration.");
////            }
//        })
//        .catch(function(error) {
//            console.error("Error:", error);
//            alert("Unknown error occurred during user registration.");
//        });
//    });
//});
