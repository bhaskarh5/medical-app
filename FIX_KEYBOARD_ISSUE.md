# 🔧 Fix: Keyboard Covering Input Fields

## ✅ **FIXED!**

I've updated the `AndroidManifest.xml` file to fix the keyboard issue!

## 🔄 **What You Need to Do Now:**

### **Step 1: Rebuild the App**

1. In Android Studio, click: **Build** → **Clean Project**
2. Wait for it to finish (5-10 seconds)
3. Click: **Build** → **Rebuild Project**
4. Wait for build to complete

### **Step 2: Run the App Again**

1. Click the green play button ▶️
2. Wait for app to launch
3. Click **"Edit Profile"**
4. Try typing in a field

### **Step 3: Test the Fix**

1. Click on the **Name** field
2. Keyboard should appear
3. **The screen should now scroll up automatically!** ✅
4. You should be able to see the field you're typing in
5. Scroll down to access other fields

---

## 🎯 **What I Changed:**

Added this line to `AndroidManifest.xml`:

```xml
android:windowSoftInputMode="adjustResize"
```

This tells Android to:

- Resize the screen when keyboard appears
- Allow scrolling to see fields behind the keyboard
- Automatically adjust the view

---

## 💡 **How to Use Edit Profile:**

### **Filling the Form:**

1. **Tap on a field** - Keyboard will appear
2. **Type your information**
3. **Swipe up/down** on the screen to scroll
4. **Fill all visible fields**
5. **Scroll down** to see more fields
6. **Continue filling** until all fields are complete
7. **Scroll to bottom** to find the "SAVE" button
8. **Click SAVE**

### **Tips:**

- ✅ You can scroll while the keyboard is open
- ✅ Use the "Next" button on keyboard to move between fields
- ✅ Swipe up to scroll down and see more fields
- ✅ The screen will adjust automatically when you tap a field

---

## 📋 **Fields You Need to Fill:**

### **Personal Information:**

1. Name - Your full name
2. Age - Your age (numbers only)
3. Blood Group - e.g., O+, A+, B+, AB+, O-, A-, B-, AB-
4. Address - Your full address

### **Medical Information:**

5. Allergies - List any allergies (or write "None")
6. Current Medications - List medications (or write "None")
7. Chronic Conditions - List conditions (or write "None")
8. Doctor Name - Your doctor's name
9. Doctor Phone - Your doctor's phone number

### **Save:**

10. Scroll all the way down
11. Click the green **"SAVE"** button

---

## 🚨 **If It Still Doesn't Work:**

### **Try 1: Restart the App**

```
Close the app completely
Run it again from Android Studio
```

### **Try 2: Restart the Emulator**

```
Close the emulator
Click the play button again
Wait for emulator to start
```

### **Try 3: Sync Gradle**

```
File → Sync Project with Gradle Files
Wait for sync to complete
Run the app again
```

### **Try 4: Use Hardware Keyboard**

If using the emulator:

1. Click on the emulator
2. Use your computer keyboard to type
3. The on-screen keyboard won't appear
4. But you can still type!

---

## 📱 **Alternative: Fill One Field at a Time**

If scrolling is difficult:

1. Click on **Name** field
2. Type your name
3. **Tap outside the field** or press Back button to close keyboard
4. **Scroll down** to see the next field
5. Click on **Age** field
6. Type your age
7. **Close keyboard again**
8. **Repeat** for each field
9. When all filled, scroll to bottom and click **"SAVE"**

---

## ✅ **After Saving:**

1. You'll see: **"Profile saved successfully"** ✅
2. App returns to main screen
3. **Wait 10 seconds** for data to sync to Firebase
4. Your Patient ID is shown on the main screen
5. **Write it down!** 📝
6. Use that Patient ID on the website

---

## 🎉 **Next Steps:**

Once you've successfully saved your profile:

1. ✅ Note your Patient ID from the app
2. ✅ Wait 10 seconds for Firebase sync
3. ✅ Open `website/test-firebase.html`
4. ✅ Click "List All Patients" - you should see YOUR data now!
5. ✅ Go to `website/index.html`
6. ✅ Enter your Patient ID
7. ✅ Click "Access Patient Information"
8. ✅ **SUCCESS!** You'll see your profile! 🎉

---

## 🔍 **Verify in Logcat:**

Want to confirm data is syncing?

1. Android Studio → **Logcat** (bottom panel)
2. In search box, type: `DataManager`
3. After clicking Save, look for:
   ```
   Data synced to Firebase successfully!
   ```
4. If you see this → Perfect! Data uploaded! ✅

---

## 📞 **Need Help?**

If you still can't fill the fields after rebuilding:

1. Take a screenshot of what you see
2. Tell me:
    - Can you see the input fields?
    - Does keyboard appear when you tap a field?
    - Can you scroll the screen?
    - What happens when you try to type?

I'll help you troubleshoot further! 😊
