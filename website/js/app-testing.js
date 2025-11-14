// ================================================
// Emergency Medical Portal - Testing Version (No Firebase)
// ================================================

// 🧪 TESTING MODE - Using local data (no Firebase required)
// Replace with your actual Patient ID from Android app

const mockPatients = {
    // 👇 YOUR PATIENT ID - Replace the data below with your ACTUAL information from Android app
    'EMG-CB0B-6C17-64F7': {
        id: 'EMG-CB0B-6C17-64F7',
        personalInfo: {
            name: 'Your Name',              // ← Replace with YOUR actual name from app
            age: 'Your Age',                // ← Replace with YOUR actual age from app
            bloodGroup: 'Your Blood Group', // ← Replace with YOUR actual blood group from app (e.g., 'O+', 'A-', 'B+')
            address: 'Your Address'         // ← Replace with YOUR actual address from app
        },
        medicalInfo: {
            allergies: 'Your Allergies (e.g., Penicillin)',        // ← Replace with YOUR allergies from app
            medications: 'Your Medications',                        // ← Replace with YOUR medications from app
            chronicConditions: 'Your Conditions',                  // ← Replace with YOUR conditions from app
            doctorName: 'Your Doctor Name',                        // ← Replace with YOUR doctor name from app
            doctorPhone: 'Your Doctor Phone'                       // ← Replace with YOUR doctor phone from app
        },
        emergencyContacts: [
            { name: 'Contact Name', phone: '+1-XXX-XXX-XXXX', isPrimary: true }  // ← Replace with YOUR emergency contact from app
        ],
        location: {
            latitude: 40.7128,   // ← Replace with your actual latitude
            longitude: -74.0060, // ← Replace with your actual longitude
            timestamp: Date.now()
        },
        emergencyActive: false
    }
};

// ===== Patient Lookup Form =====
const patientLookupForm = document.getElementById('patientLookupForm');
if (patientLookupForm) {
    patientLookupForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const patientId = document.getElementById('patientId').value.trim().toUpperCase();
        const errorDiv = document.getElementById('lookupError');
        
        console.log('Looking up Patient ID:', patientId);
        console.log('Available IDs:', Object.keys(mockPatients));
        
        // Validate format
        const patientIdPattern = /^EMG-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$/;
        if (!patientIdPattern.test(patientId)) {
            console.log('Format validation failed');
            showError(errorDiv, 'Invalid Patient ID format. Expected format: EMG-XXXX-XXXX-XXXX');
            return;
        }
        
        console.log('Format validation passed');
        
        // Check if patient exists in mock database
        if (mockPatients[patientId]) {
            console.log('Patient found!');
            // Patient found! Save to sessionStorage
            sessionStorage.setItem('currentPatientId', patientId);
            sessionStorage.setItem('patientData', JSON.stringify(mockPatients[patientId]));
            
            // Redirect to patient details page
            window.location.href = 'patient-details.html';
        } else {
            console.log('Patient not found in database');
            console.log('Searched for:', patientId);
            console.log('Available:', Object.keys(mockPatients));
            showError(errorDiv, 'Patient ID not found. Please check the ID and try again.');
        }
    });
}

// ===== Helper Functions =====
function showError(element, message) {
    element.textContent = message;
    element.style.display = 'block';
    element.className = 'error-message';
    
    // Auto-hide after 5 seconds
    setTimeout(function() {
        element.style.display = 'none';
    }, 5000);
}

function showSuccess(element, message) {
    element.textContent = message;
    element.style.display = 'block';
    element.className = 'success-message';
}

// ===== Demo Note =====
document.addEventListener('DOMContentLoaded', function() {
    const quickLookup = document.querySelector('.quick-lookup .card-body');
    if (quickLookup) {
        const demoNote = document.createElement('div');
        demoNote.style.cssText = 'background: #fef3c7; padding: 12px; border-radius: 8px; margin-top: 12px; font-size: 0.9rem; border-left: 4px solid #f59e0b;';
        demoNote.innerHTML = `
            <strong>🧪 TESTING MODE</strong><br>
            Your Patient ID: <strong>EMG-CB0B-6C17-64F7</strong><br>
            Enter this ID above to test!
        `;
        quickLookup.appendChild(demoNote);
    }
});

// Export for patient-details page
window.mockPatients = mockPatients;
