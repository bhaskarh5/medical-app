// ================================================
// Emergency Medical Portal - Main App JavaScript
// ================================================

// Note: Firebase is initialized in firebase-config.js
// The 'database' variable is available globally

// Mock hospital credentials
const mockHospitals = {
    'HOSP-001': {
        id: 'HOSP-001',
        email: 'admin@cityhospital.com',
        password: 'password123', // In real app, this would be hashed
        name: 'City General Hospital'
    },
    'HOSP-002': {
        id: 'HOSP-002',
        email: 'staff@springfieldhospital.com',
        password: 'password123',
        name: 'Springfield Medical Center'
    }
};

// ===== Patient Lookup Form =====
const patientLookupForm = document.getElementById('patientLookupForm');
if (patientLookupForm) {
    patientLookupForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const patientId = document.getElementById('patientId').value.trim().toUpperCase();
        const errorDiv = document.getElementById('lookupError');
        const submitButton = this.querySelector('button[type="submit"]');
        
        console.log('🔍 Looking up Patient ID:', patientId);
        
        // Validate format
        const patientIdPattern = /^EMG-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$/;
        if (!patientIdPattern.test(patientId)) {
            console.error('❌ Invalid Patient ID format:', patientId);
            showError(errorDiv, 'Invalid Patient ID format. Expected format: EMG-XXXX-XXXX-XXXX');
            return;
        }
        
        // Show loading state
        submitButton.disabled = true;
        submitButton.innerHTML = '<span>🔍 Searching...</span>';
        
        console.log('📡 Querying Firebase at path:', 'patients/' + patientId);
        console.log('🔗 Database URL:', firebase.app().options.databaseURL);
        
        // Query Firebase for patient data
        database.ref('patients/' + patientId).once('value')
            .then(function(snapshot) {
                submitButton.disabled = false;
                submitButton.innerHTML = '<span>🚨 Access Patient Information</span>';
                
                console.log('📦 Firebase response received');
                console.log('🔍 Snapshot exists?', snapshot.exists());
                
                if (snapshot.exists()) {
                    // Patient found! Save to sessionStorage
                    const patientData = snapshot.val();
                    sessionStorage.setItem('currentPatientId', patientId);
                    sessionStorage.setItem('patientData', JSON.stringify(patientData));
                    
                    console.log('✅ Patient found in Firebase:', patientId);
                    console.log('📋 Patient data:', patientData);
                    
                    // Redirect to patient details page
                    window.location.href = 'patient-details.html';
                } else {
                    console.log('❌ Patient not found in Firebase:', patientId);
                    console.log('💡 Tip: Check if data exists in Firebase Console');
                    console.log('💡 Go to: https://console.firebase.google.com → Realtime Database → Data');
                    
                    showError(errorDiv, 
                        'Patient ID not found. Please check the ID and try again.<br><br>' +
                        '<small>💡 <strong>Troubleshooting:</strong><br>' +
                        '1. Make sure you saved your profile in the Android app<br>' +
                        '2. Wait 10 seconds after saving for data to sync<br>' +
                        '3. Check Firebase Console to verify data exists<br>' +
                        '4. Open <a href="test-firebase.html" style="color: #2563eb;">test-firebase.html</a> to diagnose</small>'
                    );
                }
            })
            .catch(function(error) {
                submitButton.disabled = false;
                submitButton.innerHTML = '<span>🚨 Access Patient Information</span>';
                
                console.error('🔥 Firebase error:', error);
                console.error('Error code:', error.code);
                console.error('Error message:', error.message);
                
                if (error.code === 'PERMISSION_DENIED') {
                    showError(errorDiv, 
                        'Permission denied. Please check Firebase database rules.<br><br>' +
                        '<small>💡 Go to Firebase Console → Realtime Database → Rules<br>' +
                        'Make sure rules allow read access to "patients" node</small>'
                    );
                } else {
                    showError(errorDiv, 
                        'Error connecting to database: ' + error.message + '<br><br>' +
                        '<small>💡 Check your internet connection and try again</small>'
                    );
                }
            });
    });
}

// ===== Hospital Login Form =====
const hospitalLoginForm = document.getElementById('hospitalLoginForm');
if (hospitalLoginForm) {
    hospitalLoginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const hospitalId = document.getElementById('hospitalId').value.trim().toUpperCase();
        const email = document.getElementById('email').value.trim().toLowerCase();
        const password = document.getElementById('password').value;
        const rememberMe = document.getElementById('rememberMe').checked;
        const errorDiv = document.getElementById('loginError');
        
        // Validate credentials
        const hospital = mockHospitals[hospitalId];
        
        if (!hospital) {
            showError(errorDiv, 'Invalid Hospital ID. Please contact support for registration.');
            return;
        }
        
        if (hospital.email !== email || hospital.password !== password) {
            showError(errorDiv, 'Invalid email or password. Please try again.');
            return;
        }
        
        // Login successful
        const loginData = {
            hospitalId: hospital.id,
            hospitalName: hospital.name,
            email: hospital.email,
            loginTime: new Date().toISOString()
        };
        
        // Save to session or local storage
        if (rememberMe) {
            localStorage.setItem('hospitalLogin', JSON.stringify(loginData));
        } else {
            sessionStorage.setItem('hospitalLogin', JSON.stringify(loginData));
        }
        
        // Show success and redirect
        showSuccess(errorDiv, 'Login successful! Redirecting to dashboard...');
        
        setTimeout(() => {
            window.location.href = 'dashboard.html'; // You can create this later
        }, 1500);
    });
}

// ===== Helper Functions =====
function showError(element, message) {
    if (!element) return;
    
    element.innerHTML = message;
    element.style.display = 'block';
    element.className = 'error-message';
    
    // Auto-hide after 10 seconds (increased from 5)
    setTimeout(function() {
        element.style.display = 'none';
    }, 10000);
}

function showSuccess(element, message) {
    if (!element) return;
    
    element.textContent = message;
    element.style.display = 'block';
    element.className = 'success-message';
}

function showLoading(element, message) {
    element.textContent = '⏳ ' + message;
    element.style.display = 'block';
    element.className = 'info-message';
    element.style.background = '#dbeafe';
    element.style.color = '#1e40af';
}

// ===== Demo Note =====
document.addEventListener('DOMContentLoaded', function() {
    console.log('🔥 Emergency Medical Portal loaded');
    console.log('📡 Firebase initialized:', !!firebase.app());
    console.log('🗄️ Database URL:', firebase.app().options.databaseURL);
    console.log('🆔 Project ID:', firebase.app().options.projectId);
    
    const quickLookup = document.querySelector('.quick-lookup .card-body');
    if (quickLookup) {
        const demoNote = document.createElement('div');
        demoNote.style.cssText = 'background: #dcfce7; padding: 12px; border-radius: 8px; margin-top: 12px; font-size: 0.9rem; border-left: 4px solid #16a34a;';
        demoNote.innerHTML = `
            <strong>✅ Connected to Firebase!</strong><br>
            Enter any Patient ID from the Android app.<br>
            All registered users will work automatically!<br>
            <small style="margin-top: 8px; display: block;">
                <a href="test-firebase.html" style="color: #15803d; text-decoration: underline;">
                    🔍 Test your Firebase connection
                </a>
            </small>
        `;
        quickLookup.appendChild(demoNote);
    }
    
    const hospitalLogin = document.querySelector('.hospital-login .card-body');
    if (hospitalLogin) {
        const demoNote = document.createElement('div');
        demoNote.style.cssText = 'background: #fef3c7; padding: 12px; border-radius: 8px; margin-top: 12px; font-size: 0.9rem; border-left: 4px solid #f59e0b;';
        demoNote.innerHTML = `
            <strong>🎯 Demo Hospital Login:</strong><br>
            Hospital ID: HOSP-001<br>
            Email: admin@cityhospital.com<br>
            Password: password123
        `;
        hospitalLogin.appendChild(demoNote);
    }
    
    // Test Firebase connection on load
    database.ref('.info/connected').once('value')
        .then(snapshot => {
            if (snapshot.val()) {
                console.log('✅ Firebase connection test: SUCCESS');
            } else {
                console.warn('⚠️ Firebase connection test: Not connected');
            }
        })
        .catch(error => {
            console.error('❌ Firebase connection test failed:', error);
        });
});

// ===== Export mock data for other pages =====
window.mockHospitals = mockHospitals;
