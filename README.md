# Emergency Medical App 🚨

A simple, offline-first Android mobile app that stores a user's medical details and allows sharing
them quickly with emergency contacts.

## Features ✨

### Personal Information

- Store name, age, blood group, and address
- Add optional profile photo
- All data visible on main screen

### Medical Details

- Track allergies (e.g., Penicillin, Peanuts)
- List current medications
- Record chronic conditions (e.g., Diabetes, Asthma)
- Save doctor's name and phone number

### Emergency Contacts

- Add multiple emergency contacts
- Set one contact as primary
- Edit and delete contacts easily
- Mark primary contact for quick emergency calls

### Emergency Actions

- **🚨 CALL EMERGENCY**: One-click button to call the primary emergency contact
- **🎤 Voice Activation**: Say "Emergency Help" to trigger emergency call hands-free
- **Share Medical Details**: Share complete medical profile via SMS/WhatsApp to all emergency
  contacts
- Large, clearly visible red emergency call button on home screen

### Voice-Activated Emergency Mode (NEW!)

- **Hands-free emergency activation**: Just say "Emergency Help"
- **Multiple trigger phrases**: "Emergency Help", "Emergency Call", "Call Emergency"
- **Instant response**: Automatically calls primary contact and sends SMS alerts
- **Accessibility focused**: Perfect for users with limited mobility or in critical situations
- **Visual feedback**: Clear indicators showing listening status

### Data Storage

- All data stored locally using SharedPreferences
- No internet connection required
- No login or account needed
- Data persists across app sessions

### UI/UX

- Clean, Material Design interface
- Large text for easy reading in emergencies
- Red emergency button prominently displayed
- Simple navigation with clear sections
- Card-based layout for organized information
- Floating action button for voice activation

## Technical Details 🔧

### Built With

- **Language**: Java
- **UI Framework**: Android Material Components
- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)
- **Data Storage**: SharedPreferences with Gson for JSON serialization
- **Location Services**: Google Play Services Location API
- **Voice Recognition**: Android SpeechRecognizer API

### Key Dependencies

- AndroidX AppCompat
- Material Components
- ConstraintLayout
- CardView
- Gson for JSON serialization
- Google Play Services Location

### Permissions Required

- `CALL_PHONE`: For emergency calling functionality
- `READ_EXTERNAL_STORAGE`: For selecting profile photo (API ≤ 32)
- `READ_MEDIA_IMAGES`: For selecting profile photo (API ≥ 33)
- `SEND_SMS`: For sharing medical details via SMS
- `ACCESS_FINE_LOCATION`: For GPS location sharing
- `ACCESS_COARSE_LOCATION`: For approximate location
- `RECORD_AUDIO`: For voice-activated emergency mode

## Project Structure 📁

```
app/
├── src/main/
│   ├── java/com/emergency/medical/
│   │   ├── MainActivity.java              # Main screen with emergency features
│   │   ├── EditProfileActivity.java       # Edit personal & medical info
│   │   ├── ManageContactsActivity.java    # Manage emergency contacts
│   │   ├── EmergencyContactAdapter.java   # RecyclerView adapter for contacts
│   │   └── data/
│   │       ├── PersonalInfo.java          # Personal info model
│   │       ├── MedicalInfo.java           # Medical info model
│   │       ├── EmergencyContact.java      # Contact model
│   │       └── DataManager.java           # Local data persistence manager
│   ├── res/
│   │   ├── layout/                        # XML layouts
│   │   ├── values/                        # Colors, strings, themes
│   │   └── drawable/                      # Icons and backgrounds
│   └── AndroidManifest.xml
└── build.gradle
```

## Setup Instructions 📲

### Prerequisites

- Android Studio (Hedgehog or later)
- JDK 8 or higher
- Android SDK with API 34

### Installation Steps

1. **Clone or open the project in Android Studio**
   ```
   File > Open > Select project directory
   ```

2. **Configure SDK Location** (if needed)
   Create `local.properties` in root directory:
   ```properties
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   ```
   (On Windows) or
   ```properties
   sdk.dir=/Users/YourUsername/Library/Android/sdk
   ```
   (On Mac)

3. **Sync Gradle**
    - Android Studio should automatically sync
    - Or click: File > Sync Project with Gradle Files

4. **Build the project**
    - Build > Make Project
    - Or use: Ctrl+F9 (Windows) / Cmd+F9 (Mac)

5. **Run on Device/Emulator**
    - Connect Android device with USB debugging enabled
    - Or start an Android emulator
    - Click Run button or press Shift+F10

## Usage Guide 📖

### First Time Setup

1. Launch the app
2. Tap "Edit Profile" to add your personal and medical information
3. Tap "Manage Emergency Contacts" to add emergency contacts
4. Mark one contact as "Primary" for quick emergency calls

### In Case of Emergency

**Option 1: Manual Activation**
1. Open the app
2. Tap the large red "🚨 CALL EMERGENCY" button to instantly call your primary contact
3. Or tap "Share Medical Details" to send your complete medical profile to all contacts

**Option 2: Voice Activation (NEW!)**

1. Open the app
2. Tap the red microphone button (floating button at bottom-right)
3. Say "Emergency Help" clearly
4. Emergency call and SMS are triggered automatically!

### Voice Commands

The app recognizes these emergency phrases:

- "Emergency Help"
- "Emergency Call"
- "Call Emergency"
- "Help Me Emergency"

For detailed voice activation instructions,
see [VOICE_ACTIVATION_GUIDE.md](VOICE_ACTIVATION_GUIDE.md)

### Updating Information

- Tap "Edit Profile" from the main screen to update personal or medical details
- Tap "Manage Emergency Contacts" to add, edit, or delete contacts
- Tap the medical information card on the main screen for quick access to edit

## Screenshots 📱

The app features:

- **Main Screen**: Emergency call button, profile summary, medical info card, and action buttons
- **Edit Profile**: Forms for personal information and medical details
- **Manage Contacts**: List of emergency contacts with add/edit/delete options

## Important Notes ⚠️

1. **Permissions**: The app will request necessary permissions at runtime
2. **Emergency Calls**: Requires CALL_PHONE permission to be granted
3. **Voice Activation**: Requires RECORD_AUDIO permission and a quiet environment
4. **Offline First**: All features work without internet connection (except voice recognition may
   need internet on some devices)
5. **Data Privacy**: All data is stored locally on your device only
6. **Backup**: Consider backing up your data manually as it's device-specific

## Future Enhancements (Potential) 🚀

- Always-on voice listening with wake word detection
- Custom voice trigger phrases
- Multi-language voice support
- Export/Import medical data
- Multiple profiles (family members)
- Medical document attachments
- Medical history timeline
- QR code for quick medical info access
- Widget for home screen
- Wearable device integration

## License 📄

This is a sample project created for educational/demonstration purposes.

## Support 💬

For issues or questions about this app, please check the code documentation or contact the
developer.

---

**Emergency Medical App** - Your medical information, always accessible when it matters most. 🏥

