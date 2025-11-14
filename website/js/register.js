// ================================================
// Emergency Medical Portal - Registration JavaScript
// ================================================

document.addEventListener('DOMContentLoaded', function() {
    const registerForm = document.getElementById('hospitalRegisterForm');
    
    if (registerForm) {
        registerForm.addEventListener('submit', handleRegistration);
    }
    
    // Add real-time validation
    addRealtimeValidation();
});

// ===== Handle Registration =====
function handleRegistration(e) {
    e.preventDefault();
    
    const errorDiv = document.getElementById('registerError');
    const successDiv = document.getElementById('registerSuccess');
    
    // Hide previous messages
    errorDiv.style.display = 'none';
    successDiv.style.display = 'none';
    
    // Get form data
    const formData = {
        hospital: {
            name: document.getElementById('hospitalName').value.trim(),
            address: document.getElementById('hospitalAddress').value.trim(),
            city: document.getElementById('city').value.trim(),
            state: document.getElementById('state').value.trim(),
            zipCode: document.getElementById('zipCode').value.trim(),
            phone: document.getElementById('hospitalPhone').value.trim(),
            email: document.getElementById('hospitalEmail').value.trim().toLowerCase(),
            website: document.getElementById('hospitalWebsite').value.trim()
        },
        admin: {
            firstName: document.getElementById('adminFirstName').value.trim(),
            lastName: document.getElementById('adminLastName').value.trim(),
            position: document.getElementById('adminPosition').value.trim(),
            email: document.getElementById('adminEmail').value.trim().toLowerCase(),
            phone: document.getElementById('adminPhone').value.trim()
        },
        security: {
            password: document.getElementById('registerPassword').value,
            confirmPassword: document.getElementById('confirmPassword').value
        },
        terms: {
            agreeTerms: document.getElementById('agreeTerms').checked,
            agreeHipaa: document.getElementById('agreeHipaa').checked
        }
    };
    
    // Validate form
    const validation = validateForm(formData);
    
    if (!validation.valid) {
        showError(errorDiv, validation.message);
        return;
    }
    
    // Submit registration (in real app, this would be an API call)
    submitRegistration(formData, successDiv, errorDiv);
}

// ===== Validate Form =====
function validateForm(data) {
    // Check required fields
    if (!data.hospital.name) {
        return { valid: false, message: 'Hospital name is required' };
    }
    
    if (!data.hospital.phone) {
        return { valid: false, message: 'Hospital phone is required' };
    }
    
    // Validate email format
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(data.hospital.email)) {
        return { valid: false, message: 'Invalid hospital email address' };
    }
    
    if (!emailRegex.test(data.admin.email)) {
        return { valid: false, message: 'Invalid administrator email address' };
    }
    
    // Validate phone format (basic)
    const phoneRegex = /^[\d\s\-\+\(\)]+$/;
    if (!phoneRegex.test(data.hospital.phone)) {
        return { valid: false, message: 'Invalid hospital phone number format' };
    }
    
    if (!phoneRegex.test(data.admin.phone)) {
        return { valid: false, message: 'Invalid administrator phone number format' };
    }
    
    // Validate password
    if (data.security.password.length < 8) {
        return { valid: false, message: 'Password must be at least 8 characters long' };
    }
    
    // Check password complexity
    const hasUppercase = /[A-Z]/.test(data.security.password);
    const hasLowercase = /[a-z]/.test(data.security.password);
    const hasNumber = /\d/.test(data.security.password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(data.security.password);
    
    if (!hasUppercase || !hasLowercase || !hasNumber || !hasSpecial) {
        return { 
            valid: false, 
            message: 'Password must contain uppercase, lowercase, number, and special character' 
        };
    }
    
    // Check password match
    if (data.security.password !== data.security.confirmPassword) {
        return { valid: false, message: 'Passwords do not match' };
    }
    
    // Check terms agreement
    if (!data.terms.agreeTerms) {
        return { valid: false, message: 'You must agree to the Terms of Service' };
    }
    
    if (!data.terms.agreeHipaa) {
        return { valid: false, message: 'You must confirm HIPAA compliance' };
    }
    
    return { valid: true };
}

// ===== Submit Registration =====
function submitRegistration(data, successDiv, errorDiv) {
    // Show loading state
    const submitBtn = document.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.disabled = true;
    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';
    
    // Simulate API call (replace with real API in production)
    setTimeout(() => {
        // Generate hospital ID
        const hospitalId = generateHospitalId();
        
        // Store registration data (in real app, send to backend)
        const registrationData = {
            hospitalId: hospitalId,
            ...data,
            registrationDate: new Date().toISOString(),
            status: 'pending' // Would be 'pending' until verified by admin
        };
        
        // Save to localStorage for demo (use API in production)
        localStorage.setItem(`hospital_${hospitalId}`, JSON.stringify(registrationData));
        
        // Show success message
        showSuccess(successDiv, `
            <strong>Registration Successful!</strong><br><br>
            Your Hospital ID is: <strong>${hospitalId}</strong><br><br>
            Please save this ID for login. You will receive a confirmation email at 
            ${data.admin.email} once your registration is verified.<br><br>
            <em>Verification typically takes 1-2 business days.</em>
        `);
        
        // Reset form
        document.getElementById('hospitalRegisterForm').reset();
        
        // Scroll to success message
        successDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
        
        // Reset button
        submitBtn.disabled = false;
        submitBtn.innerHTML = originalText;
        
        // Redirect to home after 5 seconds
        setTimeout(() => {
            window.location.href = 'index.html';
        }, 5000);
        
    }, 2000); // Simulate network delay
}

// ===== Generate Hospital ID =====
function generateHospitalId() {
    const prefix = 'HOSP';
    const number = String(Math.floor(Math.random() * 9000) + 1000); // 1000-9999
    return `${prefix}-${number}`;
}

// ===== Add Real-time Validation =====
function addRealtimeValidation() {
    // Password strength indicator
    const passwordInput = document.getElementById('registerPassword');
    if (passwordInput) {
        passwordInput.addEventListener('input', function() {
            const strength = checkPasswordStrength(this.value);
            const small = this.parentElement.querySelector('small');
            
            if (small) {
                small.style.color = strength.color;
                small.textContent = strength.message;
            }
        });
    }
    
    // Confirm password match
    const confirmPasswordInput = document.getElementById('confirmPassword');
    if (confirmPasswordInput && passwordInput) {
        confirmPasswordInput.addEventListener('input', function() {
            const password = passwordInput.value;
            const confirm = this.value;
            
            if (confirm.length > 0) {
                if (password === confirm) {
                    this.style.borderColor = '#16a34a';
                } else {
                    this.style.borderColor = '#dc2626';
                }
            } else {
                this.style.borderColor = '';
            }
        });
    }
    
    // Email validation
    const emailInputs = document.querySelectorAll('input[type="email"]');
    emailInputs.forEach(input => {
        input.addEventListener('blur', function() {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (this.value && !emailRegex.test(this.value)) {
                this.style.borderColor = '#dc2626';
            } else {
                this.style.borderColor = '';
            }
        });
    });
}

// ===== Check Password Strength =====
function checkPasswordStrength(password) {
    if (password.length === 0) {
        return { color: '#94a3b8', message: 'Must contain uppercase, lowercase, number, and special character' };
    }
    
    if (password.length < 8) {
        return { color: '#dc2626', message: 'Too short (minimum 8 characters)' };
    }
    
    const hasUppercase = /[A-Z]/.test(password);
    const hasLowercase = /[a-z]/.test(password);
    const hasNumber = /\d/.test(password);
    const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password);
    
    const strength = [hasUppercase, hasLowercase, hasNumber, hasSpecial].filter(Boolean).length;
    
    if (strength < 4) {
        const missing = [];
        if (!hasUppercase) missing.push('uppercase');
        if (!hasLowercase) missing.push('lowercase');
        if (!hasNumber) missing.push('number');
        if (!hasSpecial) missing.push('special character');
        
        return { 
            color: '#f59e0b', 
            message: `Missing: ${missing.join(', ')}` 
        };
    }
    
    if (password.length < 12) {
        return { color: '#16a34a', message: 'Good password' };
    }
    
    return { color: '#16a34a', message: 'Strong password!' };
}

// ===== Helper Functions =====
function showError(element, message) {
    element.innerHTML = message;
    element.style.display = 'block';
    element.scrollIntoView({ behavior: 'smooth', block: 'center' });
    
    // Auto-hide after 7 seconds
    setTimeout(() => {
        element.style.display = 'none';
    }, 7000);
}

function showSuccess(element, message) {
    element.innerHTML = message;
    element.style.display = 'block';
}
