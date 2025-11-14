# 🚨 "It Worked Yesterday, Now It's Broken"

## Common Causes When System Stops Working Suddenly

### 🔥 **Issue 1: Firebase Database Rules Expired (MOST COMMON!)**

Firebase test mode rules **automatically expire after 30 days**!

**Check This First:**

1. Go to: https://console.firebase.google.com
2. Select project: `emergencymedical-a43a9`
3. Click: **Realtime Database** → **Rules** tab
4. Look at the top of the page for a **warning banner** that says:
   > ⚠️ "Your rules are expired" or "Rules expired on [date]"

**Fix:**

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

**Click "Publish"** button!

---

### 📱 **Issue 2: Android App Data Cleared**

Did you:

- Uninstall/reinstall the app?
- Clear app data?
- Switch to a different emulator/device?
- Factory reset your device?

**What happens:**

- App generates a **NEW Patient ID**
- Old Patient ID from yesterday is still in Firebase
- But you're trying to look up the NEW Patient ID (which doesn't exist yet)

**Fix:**

1. Open Android app
2. Look at the Patient ID on main screen
3. **Is it different from yesterday?**
4. If YES → You need to save profile again:
    - Edit Profile → Fill all fields → Save
    - Wait 10 seconds for sync
5. Use the NEW Patient ID on website

---

### 🌐 **Issue 3: Internet Connection Issues**

**On Android App:**

- Emulator lost internet connection
- Wifi disconnected
- Mobile data turned off

**On Website:**

- Your computer lost internet
- Firewall blocking Firebase
- VPN interfering

**Quick Test:**

- Open browser
- Go to: https://console.firebase.google.com
- Can you access it?
- If NO → Internet issue

---

### 🔧 **Issue 4: Firebase Quota Exceeded**

Firebase free tier limits:

- 50,000 reads per day
- 20,000 writes per day
- 10 GB bandwidth per month

**Check:**

1. Firebase Console → Realtime Database → Usage tab
2. Look for warning: "Quota exceeded"

**Unlikely** unless you've been testing A LOT!

---

### 🗄️ **Issue 5: Data Accidentally Deleted**

Did someone:

- Delete data in Firebase Console?
- Run a script that cleared the database?
- Test with the app and delete patients?

**Check:**

1. Firebase Console → Realtime Database → Data tab
2. Expand "patients" node
3. Is your Patient ID still there?
4. If NO → Data was deleted, need to save profile again

---

### 🔑 **Issue 6: Patient ID Changed**

The app generates a Patient ID **once on first install**.

**If the ID changed, it means:**

- App was uninstalled
- App data was cleared
- Using a different device/emulator

**Your old data is still in Firebase** with the old ID!

**Option A: Find Old ID**

1. Open `website/test-firebase.html`
2. Click "List All Patients"
3. See all Patient IDs in your database
4. Try each one on the website

**Option B: Use New ID**

1. Save profile again in Android app
2. Use the new Patient ID shown in app

---

## ⚡ Quick Diagnosis Steps

### **Step 1: Check Firebase Rules (30 seconds)**

```
Firebase Console → Realtime Database → Rules
Look for expiration warning at the top
If expired → Update rules → Publish
```

### **Step 2: Check Patient ID (30 seconds)**

```
Android app → Main screen → Note the Patient ID
Website test page → List All Patients
Is your ID in the list?
If NO → Save profile again in app
```

### **Step 3: Check Internet (10 seconds)**

```
Emulator/Phone → Open browser → Visit google.com
Your computer → Open browser → Visit firebase.google.com
Both working? → Continue to Step 4
```

### **Step 4: Check Firebase Data (1 minute)**

```
Firebase Console → Realtime Database → Data tab
Expand "patients" node
Is there any data?
If NO → All data was deleted, save profile again
If YES → Check if your Patient ID is there
```

### **Step 5: Check Browser Console (1 minute)**

```
Open website → Press F12
Click "Console" tab
Try looking up Patient ID
What error do you see?

"PERMISSION_DENIED" → Rules expired (Issue #1)
"Patient not found" → Wrong ID or data deleted (Issue #2 or #5)
"Network error" → Internet issue (Issue #3)
```

---

## 🎯 Most Likely Scenario

Since it worked yesterday, **90% chance it's one of these:**

### **Scenario A: Rules Expired**

```
Yesterday: Rules allowed read/write
Today: Rules expired
Fix: Update rules in Firebase Console
```

### **Scenario B: New Patient ID**

```
Yesterday: Used Patient ID: EMG-AAAA-BBBB-CCCC
Today: App shows: EMG-DDDD-EEEE-FFFF (different!)
Fix: Use the NEW Patient ID, or save profile with new ID
```

### **Scenario C: App Data Cleared**

```
Yesterday: Had profile saved
Today: Profile is empty in app
Fix: Fill and save profile again
```

---

## 🔍 Debug Right Now

### **Test 1: Open the diagnostic page**

```
1. Open: website/test-firebase.html
2. Click "Test Connection"
   - If ❌ → Internet or Firebase down
   - If ✅ → Continue

3. Click "Check Rules"
   - If "PERMISSION_DENIED" → Rules expired!
   - If ✅ → Rules are fine

4. Click "List All Patients"
   - If "No patients found" → All data deleted
   - If "Found X patients" → Data exists, copy exact IDs
```

### **Test 2: Check Patient ID in app**

```
1. Open Android app
2. Look at Patient ID on main screen
3. Write it down EXACTLY
4. Compare with IDs from Test 1
5. Are they the same?
```

### **Test 3: Check Firebase Console**

```
1. Go to console.firebase.google.com
2. Check for warning banners (rules expired?)
3. Realtime Database → Data → patients
4. Is data there?
```

---

## 🚨 Emergency Quick Fix

If you just want it working again **right now**:

### **1. Update Firebase Rules**

```
Firebase Console → Realtime Database → Rules
Paste this:
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
Click "Publish"
Wait 10 seconds
```

### **2. Resave Profile in App**

```
Android app → Edit Profile
Fill all fields
Click Save
Wait 10 seconds
```

### **3. Get Fresh Patient ID**

```
Android app → Main screen → Copy Patient ID
Paste into website
Should work now!
```

---

## 📊 Compare Yesterday vs Today

| What to Check | Yesterday | Today | Action |
|--------------|-----------|-------|--------|
| Patient ID in app | EMG-XXXX-... | EMG-YYYY-...? | If different → Use new ID |
| Firebase has data | Yes | ? | Check Firebase Console |
| Firebase rules | Active | Expired? | Update rules |
| Internet working | Yes | ? | Test connection |
| Profile saved in app | Yes | ? | Check app, resave if needed |

---

## ✅ Verification

**After fixing, verify everything works:**

1. ✅ Firebase Console shows no warning banners
2. ✅ Firebase rules are published (not expired)
3. ✅ Data exists under "patients" in Firebase
4. ✅ Android app shows Patient ID on main screen
5. ✅ Profile is filled in app (not empty)
6. ✅ `test-firebase.html` → "List All Patients" finds data
7. ✅ Website can look up Patient ID successfully

---

## 🎯 The Fix (99% of the time)

**If it worked yesterday but not today, it's almost certainly:**

1. **Firebase database rules expired** (30-day test mode)
    - Update the rules
    - Click "Publish"
    - **This is the #1 cause!**

2. **Patient ID changed** (app was reinstalled)
    - Use the new Patient ID from the app
    - OR find old ID in Firebase Console
    - **This is the #2 cause!**

**Try both of these first!**

---

## 📞 Still Not Working?

Run these commands and tell me the results:

1. Open `website/test-firebase.html`
2. Click ALL the test buttons
3. Screenshot the results
4. Open browser console (F12)
5. Try looking up Patient ID
6. Screenshot any errors

The detailed error messages will show exactly what changed!
