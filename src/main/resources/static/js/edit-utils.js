export function initFieldSync(mapping) {
    Object.keys(mapping).forEach(srcId => {
        const srcElement = document.getElementById(srcId);
        const dstElement = document.getElementById(mapping[srcId]);
        srcElement.addEventListener('input', function () {
            dstElement.textContent = srcElement.value;
        });
    });
}

export function initTags() {
    const tagsInput = document.getElementById('tags-input');
    const tagsContainer = document.getElementById('tags-container');
    const cardTagsContainer = document.getElementById('card-tags');
    const tagsHiddenInput = document.getElementById('tags');
    let tags = tagsHiddenInput.value ? tagsHiddenInput.value.split(',') : [];

    function createTagElement(tag) {
        const tagElement = document.createElement('span');
        tagElement.classList.add('tag');
        tagElement.textContent = tag;

        tagElement.addEventListener('click', () => {
            tags = tags.filter(t => t !== tag);
            updateTags();
        });

        return tagElement;
    }

    function updateTags() {
        tagsContainer.innerHTML = '';
        cardTagsContainer.innerHTML = '';
        tags.forEach(tag => {
            const tagElement = createTagElement(tag);
            tagsContainer.appendChild(tagElement);
            const cardTagElement = document.createElement('span');
            cardTagElement.textContent = `#${tag}`;
            cardTagsContainer.appendChild(cardTagElement);
        });
        tagsHiddenInput.value = tags.join(',');
    }

    tagsInput.addEventListener('keydown', function (event) {
        if (event.key === 'Enter' && tagsInput.value.trim() !== '') {
            event.preventDefault();
            const newTag = tagsInput.value.trim();
            if (!tags.includes(newTag)) {
                tags.push(newTag);
                updateTags();
            }
            tagsInput.value = '';
        }
    });

    // Initialize tags on page load
    updateTags();
}

export function initImageUpload() {
    const avatarInput = document.getElementById('image-input');
    const avatarDropArea = document.getElementById('image-area');
    const avatarContent = document.getElementById('image-content');
    const cardAvatar = document.getElementById('image');

    avatarDropArea.addEventListener('click', () => avatarInput.click());

    avatarDropArea.addEventListener('dragover', (event) => {
        event.preventDefault();
        avatarDropArea.classList.add('dragover');
    });

    avatarDropArea.addEventListener('dragleave', () => {
        avatarDropArea.classList.remove('dragover');
    });

    avatarDropArea.addEventListener('drop', (event) => {
        event.preventDefault();
        avatarDropArea.classList.remove('dragover');
        const files = event.dataTransfer.files;
        if (files.length > 0) {
            avatarInput.files = files;
            handleAvatarUpload(files[0]);
        }
    });

    avatarInput.addEventListener('change', () => {
        if (avatarInput.files.length > 0) {
            handleAvatarUpload(avatarInput.files[0]);
        }
    });

    function handleAvatarUpload(file) {
        const reader = new FileReader();
        reader.onload = (event) => {
            const img = document.createElement('img');
            img.src = event.target.result;
            img.style.display = 'none'
            avatarContent.innerHTML = '';
            avatarContent.appendChild(img);
            cardAvatar.src = event.target.result;
        };
        reader.readAsDataURL(file);
    }
}
