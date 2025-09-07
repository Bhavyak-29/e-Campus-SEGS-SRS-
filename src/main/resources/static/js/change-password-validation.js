function checkLength(pObject, pString) {
    if (pString.length < 6) {
        alert(pObject.name + " length must be at least 6 characters");
        return false;
    }
    return true;
}

function isAlphaNumeric(s) {
    const chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+=~`[{]}\\|:;?/>.<,\"";
    let specialCharCount = 0;

    for (let i = 0; i < s.length; i++) {
        const temp = s[i];
        if (!chars.includes(temp)) {
            alert("Please remove space(s); they are not allowed.");
            return false;
        } else if (isSpecialCharacter(temp)) {
            specialCharCount++;
        }
    }

    if (specialCharCount < 2) {
        alert("Please enter at least 2 non-alphanumeric characters");
        return false;
    }

    return true;
}

function isSpecialCharacter(s) {
    const specialChars = "!@#$%^&*()_+=~`[{]}\\|:;'?/>.<,\"";
    return specialChars.includes(s);
}

function fncChangePassword() {
    const form = document.forms["frmChangePassWord"];
    const oldPassword = form.oldpassword.value;
    const newPassword = form.newpassword.value;
    const reenterPassword = form.reenterpassword.value;

    if (oldPassword === "") {
        alert("Please enter Old Password");
        form.oldpassword.focus();
        return false;
    }

    if (newPassword === "") {
        alert("Please enter a new password");
        form.newpassword.focus();
        return false;
    }

    if (reenterPassword === "") {
        alert("Please re-enter the password");
        form.reenterpassword.focus();
        return false;
    }

    if (newPassword !== reenterPassword) {
        alert("Password and Re-entered password do not match");
        form.reenterpassword.focus();
        return false;
    }

    if (!checkLength(form.newpassword, newPassword) || !checkLength(form.reenterpassword, reenterPassword)) {
        return false;
    }

    if (!isAlphaNumeric(newPassword) || !isAlphaNumeric(reenterPassword)) {
        return false;
    }

    form.submit();
}
