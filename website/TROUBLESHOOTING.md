# 🔧 Troubleshooting Guide - "Patient ID Not Found" Error

## 🎯 Quick Diagnosis

**Open this page to test your Firebase connection:**

```
website/test-firebase.html
```

This will show you exactly what's wrong!

---

## ✅ Common Issues & Solutions

### **Issue 1: No Data in Firebase Database**

**Symptoms:**

- Website shows "Patient ID not found"
- Firebase Console shows empty database

**Solution:**

1. Open Android app
2. Go to "Edit Profile"
3. Fill in ALL fields (Name, Age, Blood Group, etc.)
4. Click **"Save"** button
5. Check if you see "Profile saved successfully" message
6. Wait 5-10 seconds for sync
7. Open Firebase Console → Realtime Database → Data tab
8. Look for your Patient ID (EMG-XXXX-XXXX-XXXX)

**If data still not appearing:**

- Check internet connection on phone/emulator
- Open Android Studio → Logcat
- Search for "DataManager" or "Firebase"
- Look for error messages

---

### **Issue 2: Firebase Database URL Mismatch**

**Symptoms:**

- App saves data locally but not to Firebase
- Website can't find any patients
- Firebase Console shows no data

**Check:**

1. Open `app/google-services.json`
2. Find the line with `"project_id"`
3. Note the project ID

4. Open `website/js/firebase-config.js`
5. Check if `databaseURL` matches this pattern:
   ```
   https://YOUR-PROJECT-ID-default-rtdb.firebaseio.com
   ```

**If they don't match:**

- Go to Firebase Console
- Project Settings → General
- Scroll to "Your apps" → Web app
- Copy the FULL config (including databaseURL)
- Paste into `website/js/firebase-config.js`

---

### **Issue 3: Firebase Database Rules**

**Symptoms:**

- Permission denied errors in browser console
- Can't read or write data

**Solution:**

1. Go to Firebase Console
2. Click "Realtime Database" → "Rules" tab
3. Make sure rules look like this:

```json
{
  "rules": {
    "patients": {
      "$patientId": {
        ".read": true,
        ".write": true
      }
    }
  }
}
```

4. Click **"Publish"**
5. Wait 10 seconds for rules to update

---

### **Issue 4: Wrong Patient ID Format**

**Symptoms:**

- "Invalid Patient ID format" error
- Can't submit the form

**Solution:**

- Patient ID must be in this format: `EMG-XXXX-XXXX-XXXX`
- All letters must be UPPERCASE
- Exactly 4 characters between each dash
- Example: `EMG-A3F9-2B4D-8C1E`

**How to find your Patient ID:**

1. Open Android app
2. Look at the main screen (blue card at top)
3. Your ID is displayed there
4. Copy it EXACTLY as shown

---

### **Issue 5: Android App Not Syncing**

**Symptoms:**

- Data saved in app but not in Firebase Console
- Logcat shows no Firebase errors

**Check if Firebase is initialized:**

1. Open `app/build.gradle`
2. Make sure these lines exist:

```gradle
plugins {
    id 'com.google.gms.google-services'  // This line is critical!
}

dependencies {
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-database'
}
```

3. Open `build.gradle` (project level)
4. Make sure this exists:

```gradle
plugins {
    id 'com.google.gms.google-services' version '4.4.0' apply false
}
```

5. Open `app/google-services.json`
6. Verify it's in the correct location: `app/google-services.json` (NOT `app/src/`)

**Rebuild the app:**

1. Android Studio → Build → Clean Project
2. Build → Rebuild Project
3. Run the app again

---

### **Issue 6: Browser Console Errors**

**How to check:**

1. Open website in browser
2. Press **F12** (Windows) or **Cmd+Option+I** (Mac)
3. Click "Console" tab
4. Look for red error messages

**Common errors:**

**Error: "Firebase: Error (auth/...")**

- Solution: You don't need Firebase Authentication for this app. Ignore this error.

**Error: "PERMISSION_DENIED"**

- Solution: See Issue #3 above (Firebase Database Rules)

**Error: "Network error"**

- Solution: Check internet connection
- Try opening Firebase Console to verify connectivity

**Error: "firebase is not defined"**

- Solution: Make sure these scripts load in `index.html`:

```html
<script src="https://www.gstatic.com/firebasejs/10.7.1/firebase-app-compat.js"></script>
<script src="https://www.gstatic.com/firebasejs/10.7.1/firebase-database-compat.js"></script>
<script src="js/firebase-config.js"></script>
```

---

## 🔍 Step-by-Step Diagnostic Process

### **Step 1: Test Firebase Config**

```
1. Open: website/test-firebase.html
2. Check section 1: Should show ✅ Firebase config loaded
3. If ❌, check your firebase-config.js file
```

### **Step 2: Test Connection**

```
1. Click "Test Connection" button
2. Should show: ✅ Connected to Firebase successfully!
3. If ❌, check internet connection and Firebase Console
```

### **Step 3: List All Patients**

```
1. Click "List All Patients" button
2. Should show: ✅ Found X patient(s)
3. If "No patients found", the database is empty
4. Go to Android app and save profile data
```

### **Step 4: Check Database Rules**

```
1. Click "Check Rules" button
2. Should show: ✅ Read permission: OK
                ✅ Write permission: OK
3. If ❌ Permission denied, update Firebase rules
```

### **Step 5: Write Test Data**

```
1. Click "Write Test Patient" button
2. Copy the generated Patient ID
3. Try looking it up on the main page
4. If it works, your Firebase is configured correctly!
```

---

## 📊 Verify Data Structure

**Android app should create this structure in Firebase:**

```json
patients/
  └── EMG-XXXX-XXXX-XXXX/
      ├── id: "EMG-XXXX-XXXX-XXXX"
      ├── personalInfo:
      │   ├── name: "John Doe"
      │   ├── age: "30"
      │   ├── bloodGroup: "O+"
      │   └── address: "123 Main St"
      ├── medicalInfo:
      │   ├── allergies: "Penicillin"
      │   ├── medications: "None"
      │   ├── chronicConditions: "None"
      │   ├── doctorName: "Dr. Smith"
      │   └── doctorPhone: "+1-555-0123"
      ├── emergencyContacts: [...]
      ├── location:
      │   ├── latitude: 40.7128
      │   ├── longitude: -74.0060
      │   └── timestamp: 1234567890
      ├── lastUpdated: 1234567890
      └── emergencyActive: false
```

**To verify:**

1. Go to Firebase Console
2. Realtime Database → Data
3. Expand "patients" node
4. Expand your Patient ID
5. Check if structure matches above

---

## 🚨 Emergency Fixes

### **Nuclear Option: Start Fresh**

If nothing works, do this:

**1. Delete Firebase data:**

```
Firebase Console → Realtime Database → Data → 
Click "patients" → Delete
```

**2. Uninstall Android app:**

```
Long-press app icon → Uninstall
```

**3. Rebuild and reinstall:**

```
Android Studio → Build → Clean Project
Build → Rebuild Project
Run app (green play button)
```

**4. Create new profile:**

```
Open app → Edit Profile → Fill all fields → Save
Wait 10 seconds
```

**5. Check Firebase:**

```
Firebase Console → Realtime Database → Data
Should see new patient data
```

**6. Test website:**

```
Open index.html → Enter Patient ID → Should work!
```

---

## 📞 Still Not Working?

### **Checklist:**

- [ ] `google-services.json` is in `app/` folder
- [ ] Firebase config is in `website/js/firebase-config.js`
- [ ] Both configs are from the SAME Firebase project
- [ ] Database rules allow read/write
- [ ] Android app shows "Profile saved successfully"
- [ ] Waited at least 10 seconds after saving
- [ ] Internet connection is working
- [ ] Tested with `test-firebase.html` page

### **Get detailed logs:**

**Android (in Android Studio):**

```
Logcat → Search for: DataManager
Look for "Data synced to Firebase successfully!"
```

**Website (in Browser):**

```
Press F12 → Console tab
Look for: "✅ Patient found in Firebase"
or errors in red
```

---

## 🎯 Most Common Solution

**90% of the time, the issue is:**

1. **No data in Firebase** because profile wasn't saved in Android app
    - Solution: Open app → Edit Profile → Fill everything → Save

2. **Wrong Firebase config** in website
    - Solution: Copy config from Firebase Console → Paste in firebase-config.js

3. **Wrong database rules**
    - Solution: Set rules to allow read/write (see Issue #3)

**Try these three things first!**

---

## ✅ Success Confirmation

**You know everything is working when:**

1. ✅ Android app shows "Profile saved successfully"
2. ✅ Firebase Console shows your patient data
3. ✅ `test-firebase.html` finds patients
4. ✅ Website successfully loads your medical info
5. ✅ All data matches between app and website

**If all 5 are checked, you're good to go!** 🎉
