# Quick Start Guide 🚀

## What You Have

A complete **Emergency Medical App** with:

- ✅ All source code files (Java)
- ✅ All layout files (XML)
- ✅ Complete data storage system
- ✅ Emergency call functionality
- ✅ Medical info sharing via SMS/WhatsApp
- ✅ Offline-first architecture

## How to Run (3 Steps)

### Step 1: Open in Android Studio

1. Open Android Studio
2. Click: **File → Open**
3. Navigate to: `C:/Users/ramya/AndroidStudioProjects/untitled`
4. Click **OK**

### Step 2: Sync & Build

Android Studio will automatically:

- Download dependencies
- Sync Gradle files
- Index the project

Wait for the status bar to show "Gradle sync finished" (usually 1-3 minutes)

### Step 3: Run the App

1. **Option A - Use Emulator:**
    - Click: Tools → Device Manager
    - Create/Start an Android Virtual Device (AVD)
    - Click the green "Run" button (▶️) or press `Shift + F10`

2. **Option B - Use Physical Device:**
    - Enable Developer Options on your Android phone
    - Enable USB Debugging
    - Connect via USB
    - Click the green "Run" button (▶️)

## App Features Overview

### Main Screen

```
🚨 CALL EMERGENCY (Big Red Button)
├── Profile Card (Name, Age, Blood Group)
├── Medical Information Card
└── Action Buttons:
    ├── Share Medical Details
    ├── Edit Profile
    └── Manage Emergency Contacts
```

### What You Can Do

**1. Add Your Information**

- Tap "Edit Profile"
- Fill in personal details (name, age, blood group, address)
- Add medical information (allergies, medications, conditions)
- Add doctor's info
- Optional: Add profile photo
- Tap "Save"

**2. Add Emergency Contacts**

- Tap "Manage Emergency Contacts"
- Tap the "+" floating button
- Enter contact name and phone number
- Check "Primary Contact" for your main emergency contact
- Tap "Add"

**3. Use Emergency Features**

- **Emergency Call**: Tap the big red button to instantly call your primary contact
- **Share Details**: Tap "Share Medical Details" to send your complete medical profile to all
  contacts

## Permissions Explained

When you first use features, the app will ask for:

| Permission | Why Needed | When Asked |
|------------|-----------|------------|
| Phone Call | To make emergency calls | When tapping Emergency Call button |
| Storage/Photos | To select profile photo | When selecting photo |
| SMS (optional) | To share via SMS | When sharing medical details |

## File Structure

```
Emergency Medical App/
├── app/
│   ├── src/main/
│   │   ├── java/com/emergency/medical/
│   │   │   ├── MainActivity.java              ← Home screen
│   │   │   ├── EditProfileActivity.java       ← Edit info screen
│   │   │   ├── ManageContactsActivity.java    ← Contacts screen
│   │   │   ├── EmergencyContactAdapter.java   ← List handler
│   │   │   └── data/
│   │   │       ├── PersonalInfo.java          ← Your data model
│   │   │       ├── MedicalInfo.java           ← Medical data model
│   │   │       ├── EmergencyContact.java      ← Contact model
│   │   │       └── DataManager.java           ← Saves/loads data
│   │   ├── res/
│   │   │   ├── layout/                        ← UI layouts
│   │   │   ├── values/                        ← Colors, strings
│   │   │   └── drawable/                      ← Icons
│   │   └── AndroidManifest.xml                ← App config
│   └── build.gradle                            ← Dependencies
├── build.gradle                                 ← Project config
└── settings.gradle                              ← Module settings
```

## Data Storage

- **Where**: Local device storage (SharedPreferences)
- **Format**: JSON
- **Security**: Private to your app only
- **Persistence**: Data survives app restarts
- **Offline**: Works without internet

## Troubleshooting

### Problem: Gradle sync fails

**Solution**:

- Check internet connection
- In Android Studio: File → Invalidate Caches → Restart

### Problem: Can't run app

**Solution**:

- Ensure Android SDK is installed
- Check `local.properties` has correct SDK path
- Try: Build → Clean Project → Rebuild Project

### Problem: "Unresolved reference" errors

**Solution**:

- Wait for Gradle sync to complete
- Try: File → Sync Project with Gradle Files

### Problem: Emulator is slow

**Solution**:

- Use a physical device
- Or create faster AVD: Use x86_64 system image with hardware acceleration

## Next Steps

1. **Test the app**:
    - Add sample personal info
    - Add test emergency contacts (use your own numbers for testing)
    - Try the emergency call feature
    - Try sharing medical details

2. **Customize** (optional):
    - Change colors in `res/values/colors.xml`
    - Modify strings in `res/values/strings.xml`
    - Adjust layouts in `res/layout/` files

3. **Build APK** (to install on other devices):
    - Build → Build Bundle(s) / APK(s) → Build APK(s)
    - Find APK in: `app/build/outputs/apk/debug/`

## Important Notes

⚠️ **For Real Emergency Use:**

- Make sure to grant all necessary permissions
- Add real emergency contact numbers
- Test the emergency call feature with non-emergency numbers first
- Keep your medical information up-to-date

✅ **Privacy:**

- All data stays on your device
- No data is sent to any server
- No internet connection required

## Need Help?

Check:

1. **README.md** - Complete documentation
2. **Code comments** - Inline explanations
3. Android Studio errors - Red text shows specific issues
4. Logcat - Shows runtime errors and messages

## Build Information

- **Language**: Java
- **Min Android**: 7.0 (API 24)
- **Target Android**: 14 (API 34)
- **Build System**: Gradle 8.0
- **Libraries**: Material Components, Gson

---

**Ready to go!** 🎉 Just open the project and click Run!
