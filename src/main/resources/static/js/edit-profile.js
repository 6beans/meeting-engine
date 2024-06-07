import {initFieldSync, initImageUpload, initTags} from "./edit-utils.js";

// Password matching functionality
function initPasswordMatching() {
    const newPassword = document.getElementById('newPassword');
    const confirmPassword = document.getElementById('confirmPassword');
    const submitButton = document.querySelector('button[type="submit"]');
    const mismatchMessage = document.getElementById('passwordMismatchMessage');

    function validatePasswords() {
        if (newPassword.value !== confirmPassword.value) {
            submitButton.disabled = true;
            mismatchMessage.style.display = 'block';
        } else {
            submitButton.disabled = false;
            mismatchMessage.style.display = 'none';
        }
    }

    newPassword.addEventListener('input', validatePasswords);
    confirmPassword.addEventListener('input', validatePasswords);
}

// Username uniqueness validation
function initUsernameValidation() {
    const userNameInput = document.getElementById('userName');
    const saveButton = document.getElementById('saveButton');
    const usernameValidationMessage = document.getElementById('usernameValidationMessage');
    const usernameLoading = document.getElementById('usernameLoading');

    const originalUsername = userNameInput.getAttribute('data-original-username');

    let usernameTimeout;

    userNameInput.addEventListener('input', function (event) {
        event.target.value = event.target.value.toLowerCase().trim();
    });

    userNameInput.addEventListener('input', function () {
        clearTimeout(usernameTimeout);
        saveButton.disabled = true;
        usernameLoading.style.display = 'block';
        if (userNameInput.value === originalUsername) {
            usernameValidationMessage.style.display = 'none';
            usernameLoading.style.display = 'none';
            saveButton.disabled = false;
            return;
        }
        usernameTimeout = setTimeout(() => {
            fetch(`/profile/validation/usernames/${userNameInput.value}`)
                .then(response => {
                    usernameLoading.style.display = 'none';
                    if (response.status === 200) {
                        usernameValidationMessage.style.display = 'none';
                        saveButton.disabled = false;
                    } else if (response.status === 409) {
                        usernameValidationMessage.textContent = 'Username is already taken.';
                        usernameValidationMessage.style.display = 'block';
                        saveButton.disabled = true;
                    } else if (response.status === 400) {
                        usernameValidationMessage.textContent = 'Invalid username format.';
                        usernameValidationMessage.style.display = 'block';
                        saveButton.disabled = true;
                    }
                });
        }, 500);
    });
}

// Email uniqueness validation
function initEmailValidation() {
    const emailInput = document.getElementById('email');
    const saveButton = document.getElementById('saveButton');
    const emailValidationMessage = document.getElementById('emailValidationMessage');
    const emailLoading = document.getElementById('emailLoading');

    const originalEmail = emailInput.getAttribute('data-original-email');

    let emailTimeout;

    emailInput.addEventListener('input', function () {
        clearTimeout(emailTimeout);
        saveButton.disabled = true;
        emailLoading.style.display = 'block';
        if (emailInput.value === originalEmail) {
            emailValidationMessage.style.display = 'none';
            emailLoading.style.display = 'none';
            saveButton.disabled = false;
            return;
        }
        emailTimeout = setTimeout(() => {
            fetch(`/profile/validation/emails/${emailInput.value}`)
                .then(response => {
                    emailLoading.style.display = 'none';
                    if (response.status === 200) {
                        emailValidationMessage.style.display = 'none';
                        saveButton.disabled = false;
                    } else if (response.status === 409) {
                        emailValidationMessage.textContent = 'Email is already in use.';
                        emailValidationMessage.style.display = 'block';
                        saveButton.disabled = true;
                    } else if (response.status === 400) {
                        emailValidationMessage.textContent = 'Invalid email format.';
                        emailValidationMessage.style.display = 'block';
                        saveButton.disabled = true;
                    }
                });
        }, 500);
    });
}

// Initialize all functionalities
document.addEventListener('DOMContentLoaded', function () {
    initTags();
    initPasswordMatching();
    initUsernameValidation();
    initEmailValidation();
    initImageUpload();
    initFieldSync({
        'userName': 'cardUserName',
        "about": "cardDesc"
    });
});