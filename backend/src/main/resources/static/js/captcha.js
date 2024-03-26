document.addEventListener("DOMContentLoaded", function() {
    window.onLoadCallBack = function() {
        if (typeof grecaptcha !== 'undefined') {
            grecaptcha.render('divRecaptcha', {
                sitekey:'6LcYmIYpAAAAABssFnlPyL7CD1pRU0I0CxO_BVMK',
                callback: successCallback,
            });
        }
    }

    function successCallback(token) {
        var loginButton = document.getElementById("loginButton");
        //Enables button once captcha gets a success call
        if (loginButton) {
            loginButton.disabled = false;
        }
    }

    // Disable login button by default
    var loginButton = document.getElementById("loginButton");
    if (loginButton) {
        loginButton.disabled = true;
    }
});