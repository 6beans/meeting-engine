document.addEventListener('DOMContentLoaded', (_) => {
    const urlParams = new URLSearchParams(window.location.search);
    const messageElement = document.getElementById('floatingMessage');

    function hideMessage() {
        messageElement.classList.add('hide');
    }

    if (urlParams.has('error')) {
        messageElement.innerText = 'Error logging in. Please try again.';
        messageElement.classList.add('show');
        setTimeout(hideMessage, 5000);
    } else if (urlParams.has('logout')) {
        messageElement.innerText = 'You have been successfully logged out.';
        messageElement.style.backgroundColor = '#4CAF50';
        messageElement.classList.add('show');
        setTimeout(hideMessage, 5000);
    }

    messageElement.addEventListener('click', hideMessage);
});
