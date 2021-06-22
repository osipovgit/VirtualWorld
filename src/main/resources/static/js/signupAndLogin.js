function auth() {
    document.getElementById("title").innerHTML = "<span class='login100-form-title p-b-26'>Authorization</span>"
    document.getElementById("action-button").innerHTML = "<button type='submit' class='login100-form-btn'>"
        + "Login</button>"
    document.getElementById("redirect").innerHTML = "<span class='txt1'>Don’t have an account? </span>"
        + "<a class='txt2' href='/join'>Sign Up</a>";
}

function join() {
    document.getElementById("title").innerHTML = "<span class='login100-form-title p-b-26'>Registration</span>"
    document.getElementById("action-button").innerHTML = "<button type='submit' class='login100-form-btn'>"
        + "Signup</button>"
    document.getElementById("redirect").innerHTML = "<span class='txt1'>Already have an account? </span>"
        + "<a class='txt2' href='/auth'>Login</a>";
}

$(document).ready(function () {
    if (window.location.pathname === "/auth") {
        auth()
    } else if (window.location.pathname === "/join") {
        join()
    }
})