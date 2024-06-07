import {initFieldSync, initImageUpload, initTags} from './edit-utils.js';

function initTitleValidation() {
    const titleInput = document.getElementById('title');
    const titleValidationMessage = document.getElementById('titleValidationMessage');

    function validateTitle() {
        const titleLength = titleInput.value.length;
        if (titleLength >= 3 && titleLength <= 36) {
            titleValidationMessage.style.display = 'none';
            return true;
        } else {
            titleValidationMessage.style.display = 'block';
            return false;
        }
    }

    titleInput.addEventListener('input', validateTitle);
    return validateTitle();
}

function initAboutValidation() {
    const aboutInput = document.getElementById('about');
    const aboutValidationMessage = document.getElementById('aboutValidationMessage');

    function validateAbout() {
        const aboutLength = aboutInput.value.length;
        if (aboutLength >= 10 && aboutLength <= 200) {
            aboutValidationMessage.style.display = 'none';
            return true;
        } else {
            aboutValidationMessage.style.display = 'block';
            return false;
        }
    }

    aboutInput.addEventListener('input', validateAbout);
    return validateAbout();
}

function initDateValidation() {
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const startDateValidationMessage = document.getElementById('startDateValidationMessage');

    function validateDates() {
        const startDate = new Date(startDateInput.value);
        const endDate = new Date(endDateInput.value);

        if (startDate < endDate) {
            startDateValidationMessage.style.display = 'none';
            return true;
        } else {
            startDateValidationMessage.style.display = 'block';
            return false;
        }
    }

    startDateInput.addEventListener('input', validateDates);
    endDateInput.addEventListener('input', validateDates);
    return validateDates();
    }

function initFormValidation() {
    const saveButton = document.getElementById('saveButton');

    function validateForm() {
        const isTitleValid = initTitleValidation();
        const isAboutValid = initAboutValidation();
        const areDatesValid = initDateValidation();

        saveButton.disabled = !(isTitleValid && isAboutValid && areDatesValid);
    }

    document.getElementById('title').addEventListener('input', validateForm);
    document.getElementById('about').addEventListener('input', validateForm);
    document.getElementById('startDate').addEventListener('input', validateForm);
    document.getElementById('endDate').addEventListener('input', validateForm);

    validateForm(); // Initial validation
}

document.addEventListener('DOMContentLoaded', function () {
    initTags();
    initImageUpload();
    initFieldSync({
        'title': 'event-title',
        'about': 'event-about',
        'startDate': 'event-startDate',
        'endDate': 'event-endDate'
    });
    initFormValidation();
});