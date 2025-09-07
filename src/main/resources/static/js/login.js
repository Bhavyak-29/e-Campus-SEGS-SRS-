// Password validation
function checkLength(Password) {
    if (Password.length < 6 || Password.length > 9) {
        alert("Your password length must be at least 6, but less than 10");
        return false;
    }
    return true;
}

// Character validation
function validateCharacters(Password) {
    return true;
}

// Image swap functions
function MM_swapImgRestore() {
    var i, x, a = document.MM_sr;
    for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++) {
        x.src = x.oSrc;
    }
}

function MM_swapImage() {
    var i, j = 0, x, a = MM_swapImage.arguments;
    document.MM_sr = new Array;
    for (i = 0; i < (a.length - 2); i += 3) {
        if ((x = document.getElementById(a[i])) !== null) {
            document.MM_sr[j++] = x;
            if (!x.oSrc) x.oSrc = x.src;
            x.src = a[i + 2];
        }
    }
}

// Form handling
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM Loaded'); // Debug message

    const loginForm = document.querySelector('form[name="login"]');
    const Username = document.querySelector('input[name="Username"]');
    const Password = document.querySelector('input[name="Password"]');

    console.log('Form found:', !!loginForm); // Debug message
    console.log('Username input found:', !!Username); // Debug message
    console.log('Password input found:', !!Password); // Debug message

    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            console.log('Form submission started');
            console.log('Username:', Username.value);
            console.log('Password length:', Password.value.length);
// Debug message
            e.preventDefault();

            if (!Username.value) {
                alert("Please enter a university ID");
                Username.focus();
                return;
            }

            if (!Password.value) {
                alert("Please enter a password");
                Password.focus();
                return;
            }

            if (!checkLength(Password.value) || !validateCharacters(Password.value)) {
                Password.focus();
                return;
            }

            console.log('Validation passed, submitting form'); // Debug message
            this.submit();
        });
    }

    // Set initial focus
    if (Username) {
        username.focus();
    }
});