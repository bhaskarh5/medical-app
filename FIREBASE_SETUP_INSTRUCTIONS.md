# 🔥 Firebase Setup Instructions

## ✅ **What I've Done:**

1. ✅ Added Firebase dependencies to `build.gradle`
2. ✅ Added Google services plugin to `app/build.gradle`
3. ✅ Updated code to use Firebase

---

## 📱 **What YOU Need to Do:**

### **STEP 1: Add google-services.json File**

**CRITICAL: You MUST do this or the app won't build!**

1. Go back to Firebase Console: https://console.firebase.google.com
2. Select your project: **EmergencyMedical**
3. Click the **⚙️ Settings icon** (top left, near Project Overview)
4. Click **"Project settings"**
5. Scroll down to **"Your apps"** section
6. Find your Android app: `com.emergency.medical`
7. Click **"Download google-services.json"**
8. **IMPORTANT:** Move this file to:
   ```
   C:\Users\ramya\AndroidStudioProjects\untitled\app\
   ```
   (Put it directly in the `app` folder, NOT in `src`)

### **File Location Should Be:**

```
untitled/
├── app/
│   ├── google-services.json  ← HERE!
│   ├── build.gradle
│   ├── src/
│   └── ...
```

---

### **STEP 2: Sync Gradle**

1. Open Android Studio
2. You'll see notification: **"Gradle files have changed..."**
3. Click **"Sync Now"**
4. Wait for sync to complete (~1-2 minutes)
5. If you see errors about google-services.json, make sure Step 1 is done!

---

### **STEP 3: Set Firebase Database Rules**

**Go to Firebase Console:**

1. Click **"Realtime Database"** in left sidebar
2. Click **"Rules"** tab
3. Replace the rules with this:

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

**Note:** These rules allow anyone to read/write. For production, you'll want to add authentication.

---

### **STEP 4: Test the Android App**

1. Run the app on emulator/device
2. Go to "Edit Profile"
3. Fill in your information (name, age, blood group, etc.)
4. Click **"Save"**
5. **Check Firebase Console:**
    - Go to "Realtime Database"
    - Click "Data" tab
    - You should see your Patient ID appear!
    - Click to expand and see your data!

---

## 🌐 **STEP 5: Update Website**

I'll create updated website files that connect to Firebase.

You need to:

1. Copy the Firebase config from Step 3 (when you registered web app)
2. I'll create a file: `website/js/firebase-config.js`
3. You'll paste your config there

---

## 🎯 **After These Steps:**

✅ Android app will upload data to Firebase  
✅ Website will read data from Firebase  
✅ ANY user who downloads app will work automatically!  
✅ No manual ID adding needed!

---

## ⚠️ **Troubleshooting:**

### **Error: "File google-services.json is missing"**

- Make sure you downloaded google-services.json
- Place it in `app/` folder (not `app/src/`)
- Sync Gradle again

### **Error: "Failed to resolve: com.google.firebase..."**

- Check internet connection
- Sync Gradle again
- Try: Build → Clean Project → Rebuild Project

### **Data not appearing in Firebase:**

- Check Firebase Console → Realtime Database → Data tab
- Make sure Rules are published (Step 3)
- Check Android app Logcat for errors

---

## 📞 **Need Help?**

If you get stuck:

1. Check Logcat in Android Studio for errors
2. Make sure google-services.json is in the right place
3. Make sure Firebase Rules are published
4. Try Clean Project and Rebuild

---

**Next: Once google-services.json is added and Gradle syncs, I'll create the Firebase-enabled
website!** 🚀
