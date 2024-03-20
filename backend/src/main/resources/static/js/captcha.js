/*document.addEventListener("DOMContentLoaded", function() {
    function successCallback(token) {
        var loginButton = document.getElementById("loginButton");
        //Enables button once captcha gets a success call
        if (loginButton) {
            loginButton.disabled = false;
        }
    }

    //Call back function
    function onLoadCallBack(){
        grecaptcha.render('divRecaptcha', {
            sitekey:'6LcYmIYpAAAAABssFnlPyL7CD1pRU0I0CxO_BVMK',
            callback: successCallback,
        });
    }

    // Disable login button by default
    var loginButton = document.getElementById("loginButton");
    if (loginButton) {
        loginButton.disabled = true;
    }

    // Call onLoadCallBack to render reCAPTCHA
    onLoadCallBack();
});*/