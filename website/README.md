# 🏥 Emergency Medical Portal - Website

## 📖 Overview

This is a simple web portal where hospital staff can access patient medical information by entering
the patient's unique ID. No login or registration required!

---

## 🚀 How to Use

### **Step 1: Open the Website**

- Double-click on `index.html`
- It will open in your web browser

### **Step 2: Enter Patient ID**

- Type the patient ID in the format: `EMG-XXXX-XXXX-XXXX`
- Click "Access Patient Information"

### **Step 3: View Medical Details**

- See complete patient profile
- View GPS location
- Call emergency contacts
- Print or share information

---

## 🎯 Demo Patient IDs (For Testing)

Try these sample IDs:

1. **`EMG-A3F9-2B4D-8C1E`** - John Doe (Active Emergency)
    - Blood Group: O+
    - Allergies: Penicillin, Peanuts

2. **`EMG-B7C2-9D5F-3A1E`** - Sarah Johnson (No Emergency)
    - Blood Group: A-
    - Allergies: None

---

## 📂 Files Structure

```
website/
├── index.html              ← Main page (start here)
├── patient-details.html    ← Patient info display
├── register.html           ← (Optional) Hospital registration
├── css/
│   └── style.css          ← All styling
└── js/
    ├── app.js             ← Main logic
    ├── patient-details.js ← Patient display
    └── register.js        ← Registration logic
```

---

## ✨ Features

✅ **Simple Patient ID Lookup** - Just enter ID and go
✅ **Complete Medical Profile** - Personal info, allergies, medications
✅ **GPS Location** - Real-time map showing patient location
✅ **Emergency Contacts** - One-click calling
✅ **Print Friendly** - Print patient info for records
✅ **Responsive Design** - Works on phone, tablet, desktop
✅ **No Login Required** - Quick access during emergencies

---

## 🔄 How to Add More Patients

Open `js/app.js` and add more entries to the `mockPatients` object:

```javascript
'EMG-YOUR-NEW-ID': {
    id: 'EMG-YOUR-NEW-ID',
    personalInfo: {
        name: 'Patient Name',
        age: '30',
        bloodGroup: 'A+',
        address: 'Patient Address'
    },
    medicalInfo: {
        allergies: 'List allergies here',
        medications: 'List medications here',
        chronicConditions: 'List conditions here',
        doctorName: 'Dr. Name',
        doctorPhone: '+1-555-0000'
    },
    emergencyContacts: [
        { name: 'Contact Name', phone: '+1-555-0000', isPrimary: true }
    ],
    location: {
        latitude: 40.7128,
        longitude: -74.0060,
        timestamp: new Date()
    },
    emergencyActive: true
}
```

---

## 🎨 Customization

### Change Colors:

Edit `css/style.css` - look for the `:root` section at the top

### Change Text:

Edit `index.html` - all text is in plain HTML

### Add Features:

Edit `js/app.js` and `js/patient-details.js`

---

## 🔮 Next Steps (Future)

To make this production-ready:

1. **Backend Server** - Store real patient data in database
2. **API Integration** - Connect Android app to website
3. **Security** - Add authentication for hospitals
4. **Real-time Updates** - Live data synchronization

---

## 📞 Current Status

✅ **Working:** Patient ID lookup with demo data
✅ **Working:** Complete medical info display
✅ **Working:** GPS location with Google Maps
✅ **Working:** Responsive design

❌ **Not Working:** Real-time database (using demo data)
❌ **Not Working:** Multiple hospitals (no login system)

---

## 🎯 Perfect For

- ✅ Demos and presentations
- ✅ Testing and development
- ✅ Proof of concept
- ✅ Local hospital trials
- ✅ Emergency response training

---

**Made with ❤️ for Emergency Medical Response**
