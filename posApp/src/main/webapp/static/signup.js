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
    console.log(json1);
    if (json1.email == "") {
        warning("Email cannot be empty");
        return false;
    }
    if (json1.password == "") {
        warning("Password cannot be empty");
        return false;
    }
    if (!isValidEmail(json1.email)) {
        warning("Invalid email format");
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

function isValidEmail(email) {
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    return emailPattern.test(email);
}

function init() {
    $('#signup').click(signupUser);
}

$(document).ready(init);
