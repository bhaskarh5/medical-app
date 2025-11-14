// ================================================
// Emergency Medical Portal - Patient Details JavaScript
// ================================================

// ===== Load Patient Data =====
document.addEventListener('DOMContentLoaded', function() {
    const patientId = sessionStorage.getItem('currentPatientId');
    const patientDataJson = sessionStorage.getItem('patientData');
    
    if (!patientId || !patientDataJson) {
        // No patient data found, redirect to home
        alert('No patient selected. Please enter a Patient ID.');
        window.location.href = 'index.html';
        return;
    }
    
    const patient = JSON.parse(patientDataJson);
    
    if (!patient) {
        alert('Patient data not found.');
        window.location.href = 'index.html';
        return;
    }
    
    // Load all patient data
    loadPatientData(patient);
    
    // Setup event listeners
    setupEventListeners(patient);
});

// ===== Load Patient Data into Page =====
function loadPatientData(patient) {
    // Patient ID and Status
    document.getElementById('patientId').textContent = patient.id;
    
    const statusText = document.getElementById('statusText');
    const statusBanner = document.querySelector('.status-banner');
    
    if (patient.emergencyActive) {
        statusText.textContent = '🔴 ACTIVE EMERGENCY';
        statusBanner.classList.add('emergency-active');
    } else {
        statusText.textContent = '🟢 NO ACTIVE EMERGENCY';
        statusBanner.classList.remove('emergency-active');
        statusBanner.style.background = 'linear-gradient(135deg, #16a34a 0%, #15803d 100%)';
    }
    
    // Timestamp
    updateTimestamp();
    
    // Personal Information
    document.getElementById('patientName').textContent = patient.personalInfo.name || 'Not provided';
    document.getElementById('patientAge').textContent = patient.personalInfo.age || 'Not provided';
    document.getElementById('bloodGroup').textContent = patient.personalInfo.bloodGroup || 'Not provided';
    document.getElementById('address').textContent = patient.personalInfo.address || 'Not provided';
    
    // Medical Information
    document.getElementById('allergies').textContent = patient.medicalInfo.allergies || 'None reported';
    document.getElementById('medications').textContent = patient.medicalInfo.medications || 'None reported';
    document.getElementById('chronicConditions').textContent = patient.medicalInfo.chronicConditions || 'None reported';
    
    // Doctor Information
    document.getElementById('doctorName').textContent = patient.medicalInfo.doctorName || 'Not provided';
    document.getElementById('doctorPhone').textContent = patient.medicalInfo.doctorPhone || 'Not provided';
    
    // Emergency Contacts
    loadEmergencyContacts(patient.emergencyContacts);
    
    // Location
    loadLocation(patient.location);
}

// ===== Load Emergency Contacts =====
function loadEmergencyContacts(contacts) {
    const contactsList = document.getElementById('emergencyContactsList');
    
    if (!contacts || contacts.length === 0) {
        contactsList.innerHTML = '<p class="loading">No emergency contacts available</p>';
        return;
    }
    
    contactsList.innerHTML = '';
    
    contacts.forEach(contact => {
        const contactDiv = document.createElement('div');
        contactDiv.className = 'contact-item' + (contact.isPrimary ? ' primary' : '');
        
        contactDiv.innerHTML = `
            <div class="contact-info">
                <h4>${contact.name}${contact.isPrimary ? ' <span class="contact-badge">PRIMARY</span>' : ''}</h4>
                <p>${contact.phone}</p>
            </div>
            <button class="btn btn-primary" onclick="callContact('${contact.phone}')">
                <i class="fas fa-phone"></i> Call
            </button>
        `;
        
        contactsList.appendChild(contactDiv);
    });
}

// ===== Load Location =====
function loadLocation(location) {
    if (!location || !location.latitude || !location.longitude) {
        document.getElementById('latitude').textContent = 'Not available';
        document.getElementById('longitude').textContent = 'Not available';
        document.getElementById('locationTime').textContent = 'Location data not available';
        return;
    }
    
    const lat = location.latitude.toFixed(6);
    const lng = location.longitude.toFixed(6);
    
    document.getElementById('latitude').textContent = lat;
    document.getElementById('longitude').textContent = lng;
    
    // Format timestamp
    const timeAgo = location.timestamp ? getTimeAgo(location.timestamp) : 'Unknown';
    document.getElementById('locationTime').textContent = `Last updated: ${timeAgo}`;
    
    // Update Google Maps link
    const mapsUrl = `https://www.google.com/maps?q=${lat},${lng}`;
    document.getElementById('googleMapsLink').href = mapsUrl;
    
    // Load map embed
    loadMapEmbed(lat, lng);
}

// ===== Load Google Maps Embed =====
function loadMapEmbed(lat, lng) {
    const mapContainer = document.getElementById('mapContainer');
    
    // Create iframe for embedded map
    const iframe = document.createElement('iframe');
    iframe.width = '100%';
    iframe.height = '400';
    iframe.frameBorder = '0';
    iframe.style.border = '0';
    iframe.loading = 'lazy';
    iframe.referrerPolicy = 'no-referrer-when-downgrade';
    iframe.src = `https://www.google.com/maps?q=${lat},${lng}&output=embed`;
    
    mapContainer.innerHTML = '';
    mapContainer.appendChild(iframe);
}

// ===== Update Timestamp =====
function updateTimestamp() {
    const timestamp = document.getElementById('timeStamp');
    const now = new Date();
    timestamp.textContent = `Last Updated: ${now.toLocaleTimeString()}`;
}

// ===== Get Time Ago =====
function getTimeAgo(timestamp) {
    const now = new Date();
    const past = new Date(timestamp);
    const diffMs = now - past;
    const diffMins = Math.floor(diffMs / 60000);
    
    if (diffMins < 1) return 'Just now';
    if (diffMins === 1) return '1 minute ago';
    if (diffMins < 60) return `${diffMins} minutes ago`;
    
    const diffHours = Math.floor(diffMins / 60);
    if (diffHours === 1) return '1 hour ago';
    if (diffHours < 24) return `${diffHours} hours ago`;
    
    const diffDays = Math.floor(diffHours / 24);
    if (diffDays === 1) return '1 day ago';
    return `${diffDays} days ago`;
}

// ===== Setup Event Listeners =====
function setupEventListeners(patient) {
    // Refresh button
    document.getElementById('refreshBtn').addEventListener('click', function() {
        this.querySelector('i').style.animation = 'spin 0.5s linear';
        setTimeout(() => {
            this.querySelector('i').style.animation = '';
            loadPatientData(patient);
            alert('Data refreshed!');
        }, 500);
    });
    
    // Print button
    document.getElementById('printBtn').addEventListener('click', function() {
        window.print();
    });
    
    // Share button
    document.getElementById('shareBtn').addEventListener('click', function() {
        const shareData = generateShareData(patient);
        
        // Try native share API (mobile)
        if (navigator.share) {
            navigator.share({
                title: `Emergency Medical Info - ${patient.id}`,
                text: shareData
            }).catch(err => console.log('Error sharing:', err));
        } else {
            // Fallback: copy to clipboard
            navigator.clipboard.writeText(shareData).then(() => {
                alert('Patient information copied to clipboard!');
            }).catch(() => {
                alert('Unable to copy. Please use print instead.');
            });
        }
    });
    
    // Call doctor button
    document.getElementById('callDoctorBtn').addEventListener('click', function() {
        callContact(patient.medicalInfo.doctorPhone);
    });
    
    // Get directions button
    document.getElementById('directionsBtn').addEventListener('click', function() {
        if (patient.location && patient.location.latitude) {
            const url = `https://www.google.com/maps/dir/?api=1&destination=${patient.location.latitude},${patient.location.longitude}`;
            window.open(url, '_blank');
        } else {
            alert('Location data not available');
        }
    });
    
    // Logout button
    document.getElementById('logoutBtn').addEventListener('click', function() {
        sessionStorage.removeItem('currentPatientId');
        sessionStorage.removeItem('patientData');
        window.location.href = 'index.html';
    });
}

// ===== Call Contact Function =====
function callContact(phone) {
    if (confirm(`Call ${phone}?`)) {
        window.location.href = `tel:${phone}`;
    }
}

// ===== Generate Share Data =====
function generateShareData(patient) {
    return `
🚨 EMERGENCY MEDICAL INFORMATION 🚨

Patient ID: ${patient.id}

PERSONAL DETAILS:
Name: ${patient.personalInfo.name}
Age: ${patient.personalInfo.age}
Blood Group: ${patient.personalInfo.bloodGroup}
Address: ${patient.personalInfo.address}

MEDICAL INFORMATION:
Allergies: ${patient.medicalInfo.allergies}
Medications: ${patient.medicalInfo.medications}
Conditions: ${patient.medicalInfo.chronicConditions}

DOCTOR:
${patient.medicalInfo.doctorName}
${patient.medicalInfo.doctorPhone}

LOCATION:
${patient.location ? `https://www.google.com/maps?q=${patient.location.latitude},${patient.location.longitude}` : 'Not available'}
    `.trim();
}

// ===== Add CSS for spin animation =====
const style = document.createElement('style');
style.textContent = `
    @keyframes spin {
        from { transform: rotate(0deg); }
        to { transform: rotate(360deg); }
    }
`;
document.head.appendChild(style);

// ===== Auto-refresh timestamp every minute =====
setInterval(updateTimestamp, 60000);
