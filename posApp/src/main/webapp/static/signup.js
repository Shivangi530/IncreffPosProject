document.addEventListener("DOMContentLoaded", function() {
    var signupForm = document.getElementById("signupForm");
    signupForm.addEventListener("submit", function(event) {
        event.preventDefault(); // Prevent form submission

        var email = document.getElementById("email").value;
        var password = document.getElementById("password").value;

        // Create an object to store form data
        var formData = {
            email: email,
            password: password
        };
        console.log("formData= ",formData);
        // Send a POST request to the signup API endpoint
        fetch("/session/signup", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(formData)
        })
        .then(function(response) {
            if (response.ok) {
                // Redirect to the login page on successful signup
                window.location.href = "/site/login";
            } else {
                // Display error message
                alert("Error occurred during user registration");
            }
        })
        .catch(function(error) {
            console.error("Error:", error);
            // Display error message
            alert("Error occurred during user registration");
        });
    });
});
