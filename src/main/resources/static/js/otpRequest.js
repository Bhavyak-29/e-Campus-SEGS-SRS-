function validatePasswords() {
        const password = document.getElementById("newPassword").value;
        const confirmPassword = document.getElementById("confirmPassword").value;
        const errorElement = document.getElementById("passwordMismatch");

        // Length check
        if (password.length < 6 || password.length > 9) {
            alert("Your password must be at least 6 characters and less than 10 characters long.");
            return false;
        }

        // Confirm match
        if (password !== confirmPassword) {
            errorElement.style.display = "block";
            return false;
        } else {
            errorElement.style.display = "none";
        }

        return true;
    }

document.addEventListener("DOMContentLoaded", () => {
    let countdown = 30;
    const countdownElement = document.getElementById("countdown");
    const resendInfo = document.getElementById("resendInfo");
    const resendMessage = document.getElementById("resendMessage");
    const params = new URLSearchParams(window.location.search);
    const univid = params.get("univid");

    const interval = setInterval(() => {
        countdown--;
        countdownElement.textContent = countdown;

        if (countdown <= 0) {
            clearInterval(interval);

            // Replace text with a button
            resendInfo.innerHTML = `<button id="resendBtn">Resend OTP</button>`;

            // Add AJAX listener to the new button
            document.getElementById("resendBtn").addEventListener("click", () => {
                fetch(`/auth/request-otp?univid=${univid}`)
                    .then(response => {
                        if (!response.ok) throw new Error("Failed to resend OTP");
                        return response.text();
                    })
                    .then(() => {
                        resendMessage.textContent = "A new OTP has been sent to your email.";
                        resendMessage.style.color = "green";
                    })
                    .catch(err => {
                        resendMessage.textContent = "Error sending OTP. Please try again.";
                        resendMessage.style.color = "red";
                    });
            });
        }
    }, 1000);
});

