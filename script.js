document.addEventListener('DOMContentLoaded', () => {
    const registrationForm = document.getElementById('registrationForm');
    const topicSelect = document.getElementById('topic');
    const confirmationMessage = document.getElementById('confirmationMessage');
    const emailInput = document.getElementById('email');
    const emailError = document.getElementById('emailError');

    // Function to fetch seminar topics from backend
    async function fetchSeminarTopics() {
        try {
            const response = await fetch('http://localhost:8080/api/topik'); // Adjust URL if necessary
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            const topics = await response.json();
            populateTopicDropdown(topics);
        } catch (error) {
            console.error('Error fetching seminar topics:', error);
            showConfirmationMessage('Gagal memuat topik seminar. Silakan coba lagi nanti.', 'error');
        }
    }

    // Function to populate the topic dropdown
    function populateTopicDropdown(topics) {
        topicSelect.innerHTML = '<option value="">Pilih Topik</option>'; // Clear existing options
        topics.forEach(topic => {
            const option = document.createElement('option');
            option.value = topic.id; // Assuming topic has an ID
            option.textContent = topic.name; // Assuming topic has a name property
            topicSelect.appendChild(option);
        });
    }

    // Function to show confirmation message
    function showConfirmationMessage(message, type) {
        confirmationMessage.textContent = message;
        confirmationMessage.className = `confirmation-message ${type}`; // Add dynamic class for styling
        confirmationMessage.classList.remove('hidden');

        // Automatically hide message after 5 seconds
        setTimeout(() => {
            confirmationMessage.classList.add('hidden');
        }, 5000);
    }

    // Function to validate email format
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }

    // Event listener for email input validation on blur
    emailInput.addEventListener('blur', () => {
        if (emailInput.value.trim() === '') {
            emailError.textContent = 'Email wajib diisi.';
            emailInput.classList.add('is-invalid'); // Add class for styling invalid input
        } else if (!validateEmail(emailInput.value)) {
            emailError.textContent = 'Format email tidak valid.';
            emailInput.classList.add('is-invalid');
        } else {
            emailError.textContent = '';
            emailInput.classList.remove('is-invalid');
        }
    });

    // Event listener for form submission
    registrationForm.addEventListener('submit', async (event) => {
        event.preventDefault(); // Prevent default form submission

        // Basic front-end validation before sending data
        let isValid = true;
        if (emailInput.value.trim() === '' || !validateEmail(emailInput.value)) {
            emailError.textContent = 'Email wajib diisi dengan format valid.';
            emailInput.classList.add('is-invalid');
            isValid = false;
        } else {
            emailError.textContent = '';
            emailInput.classList.remove('is-invalid');
        }

        if (!isValid) {
            showConfirmationMessage('Mohon lengkapi semua data dengan benar.', 'error');
            return;
        }

        const formData = new FormData(registrationForm);
        const registrationData = {
            fullName: formData.get('fullName'),
            email: formData.get('email'),
            instance: formData.get('instance'),
            topicId: formData.get('topic'), // Assuming topic dropdown returns topic ID
            paymentMethod: formData.get('paymentMethod')
        };

        try {
            const response = await fetch('http://localhost:8080/api/pendaftaran', { // Adjust URL if necessary
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(registrationData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || `HTTP error! status: ${response.status}`);
            }

            const result = await response.json();
            showConfirmationMessage('Pendaftaran berhasil! ' + (result.message || ''), 'success');
            registrationForm.reset(); // Clear the form
        } catch (error) {
            console.error('Error submitting registration:', error);
            showConfirmationMessage('Pendaftaran gagal: ' + error.message, 'error');
        }
    });

    // Initial call to fetch seminar topics when the page loads
    fetchSeminarTopics();
});