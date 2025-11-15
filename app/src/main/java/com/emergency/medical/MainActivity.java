package com.emergency.medical;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.emergency.medical.data.DataManager;
import com.emergency.medical.data.EmergencyContact;
import com.emergency.medical.data.MedicalInfo;
import com.emergency.medical.data.PersonalInfo;
import com.emergency.medical.data.SettingsManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.Priority;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CALL_PERMISSION = 1;
    private static final int REQUEST_SMS_PERMISSION = 2;
    private static final int REQUEST_CALL_AND_SMS_PERMISSION = 3;
    private static final int REQUEST_LOCATION_PERMISSION = 4;

    private static final String SMS_SENT = "SMS_SENT";
    private static final String SMS_DELIVERED = "SMS_DELIVERED";

    private DataManager dataManager;
    private SettingsManager settingsManager;
    private TextView tvWelcome, tvName, tvDetectionStatus, tvLocationStatus, tvContactsCount, tvPatientId, tvAge, tvBloodGroup;
    private ImageView ivProfilePhoto, ivDetectionIcon;
    private ImageButton btnSettings;
    private MaterialCardView btnDetection;
    private MaterialCardView cardLocationStatus;
    private MaterialCardView cardEmergencyContacts;
    private BottomNavigationView bottomNavigationView;
    private SwitchCompat switchLocation;
    private CompoundButton.OnCheckedChangeListener locationToggleListener;
    private MaterialButton btnEmergencyCall;
    private MaterialButton btnTestAccident;
    private MaterialButton btnShareDetails;
    private MaterialButton btnEditProfile;
    private MaterialButton btnManageContacts;
    private MaterialButton btnFindHospitals;

    // Flag to track which action triggered permission request
    private boolean isEmergencyCallPending = false;

    // Broadcast receivers for SMS status
    private BroadcastReceiver sentReceiver;
    private BroadcastReceiver deliveredReceiver;

    // Location client
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;

    // Text-to-Speech for auto-message
    private TextToSpeech textToSpeech;
    private boolean isTtsReady = false;
    private String pendingPhoneNumber = null;

    // Accident detection variables
    private CountDownTimer accidentCountdown;
    private AlertDialog countdownDialog;
    private ToneGenerator siren;
    private boolean isAccidentMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataManager = new DataManager(this);
        settingsManager = new SettingsManager(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initializeViews();
        loadData();
        setupListeners();
        setupSmsReceivers();
        if (settingsManager.isAutoLocationEnabled()) {
            requestLocationPermissionAndFetch();
        }
        setupTextToSpeech();

        // Initialize accident detection variables
        siren = new ToneGenerator(AudioManager.STREAM_ALARM, 100);

        // Apply premium animations
        startPremiumAnimations();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvName = findViewById(R.id.tvName);
        tvDetectionStatus = findViewById(R.id.tvDetectionStatus);
         tvLocationStatus = findViewById(R.id.tvLocationStatus);
        tvContactsCount = findViewById(R.id.tvContactsCount);
        tvPatientId = findViewById(R.id.tvPatientId);
        tvAge = findViewById(R.id.tvAge);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);

        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        ivDetectionIcon = findViewById(R.id.ivDetectionIcon);
        btnDetection = findViewById(R.id.btnDetection);
        cardLocationStatus = findViewById(R.id.cardLocationStatus);
        cardEmergencyContacts = findViewById(R.id.cardEmergencyContacts);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        switchLocation = findViewById(R.id.switchLocation);

        btnSettings = findViewById(R.id.btnSettings);
        btnEmergencyCall = findViewById(R.id.btnEmergencyCall);
        btnTestAccident = findViewById(R.id.btnTestAccident);
        btnShareDetails = findViewById(R.id.btnShareDetails);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnManageContacts = findViewById(R.id.btnManageContacts);
        btnFindHospitals = findViewById(R.id.btnFindHospitals);
    }

    private void loadData() {
        PersonalInfo personalInfo = dataManager.getPersonalInfo();

        // Display Patient ID
        String patientId = dataManager.getPatientId();
        if (tvPatientId != null) {
            if (patientId == null || patientId.isEmpty()) {
                tvPatientId.setText(getString(R.string.patient_id_placeholder));
            } else {
                tvPatientId.setText(getString(R.string.emergency_id_format, patientId));
            }
        }

        if (personalInfo.getName().isEmpty()) {
            tvName.setText(R.string.tap_to_view);
            if (tvAge != null) {
                tvAge.setText("");
            }
            if (tvBloodGroup != null) {
                tvBloodGroup.setText("");
            }
        } else {
            tvName.setText(personalInfo.getName());
            if (tvAge != null) {
                String ageText = personalInfo.getAge().isEmpty()
                        ? getString(R.string.age_hint)
                        : getString(R.string.age_display_format, personalInfo.getAge());
                tvAge.setText(ageText);
            }
            if (tvBloodGroup != null) {
                String bloodGroupText = personalInfo.getBloodGroup().isEmpty()
                        ? getString(R.string.blood_group_hint)
                        : getString(R.string.blood_group_display_format, personalInfo.getBloodGroup());
                tvBloodGroup.setText(bloodGroupText);
            }

            if (!personalInfo.getPhotoUri().isEmpty()) {
                try {
                    ivProfilePhoto.setImageURI(Uri.parse(personalInfo.getPhotoUri()));
                } catch (Exception e) {
                    ivProfilePhoto.setImageResource(R.drawable.ic_person);
                }
            }
        }

        List<EmergencyContact> contacts = dataManager.getEmergencyContacts();
        if (tvContactsCount != null) {
            if (contacts.isEmpty()) {
                tvContactsCount.setText(getString(R.string.no_contacts_msg));
            } else {
                tvContactsCount.setText(getString(R.string.contacts_count, contacts.size()));
            }
        }
        boolean isAutoLocationEnabled = settingsManager.isAutoLocationEnabled();
        if (switchLocation != null) {
            if (locationToggleListener != null) {
                switchLocation.setOnCheckedChangeListener(null);
            }
            switchLocation.setChecked(isAutoLocationEnabled);
            if (locationToggleListener != null) {
                switchLocation.setOnCheckedChangeListener(locationToggleListener);
            }
        }
        updateLocationStatusText(isAutoLocationEnabled);
    }

    private void setupListeners() {
        btnEmergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEmergencyCall();
            }
        });

        btnTestAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAccidentCountdown();
            }
        });

        btnShareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDetailsWithContacts();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditProfile();
            }
        });
        btnManageContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageContacts();
            }
        });
        btnFindHospitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findNearestHospitals();
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        cardEmergencyContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openManageContacts();
            }
        });

        cardLocationStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLocation.toggle();
            }
        });

        locationToggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsManager.setAutoLocationEnabled(isChecked);
                updateLocationStatusText(isChecked);

                if (isChecked) {
                    requestLocationPermissionAndFetch();
                    Toast.makeText(MainActivity.this,
                            R.string.settings_auto_location_enabled,
                            Toast.LENGTH_SHORT).show();
                } else {
                    currentLocation = null;
                    Toast.makeText(MainActivity.this,
                            R.string.settings_auto_location_disabled,
                            Toast.LENGTH_SHORT).show();
                }
            }
        };
        switchLocation.setOnCheckedChangeListener(locationToggleListener);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                return true;
            } else if (itemId == R.id.nav_history) {
                openHistory();
                return true;
            } else if (itemId == R.id.nav_alerts) {
                openAlerts();
                return true;
            } else if (itemId == R.id.nav_settings) {
                openSettings();
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void setupSmsReceivers() {
        // Receiver for SMS sent status
        sentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case RESULT_OK:
                        Log.d(TAG, "SMS sent successfully");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.e(TAG, "SMS generic failure");
                        Toast.makeText(context, "SMS send failed - generic error", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Log.e(TAG, "SMS no service");
                        Toast.makeText(context, "No SMS service available", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Log.e(TAG, "SMS null PDU");
                        Toast.makeText(context, "SMS send failed - null PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Log.e(TAG, "SMS radio off");
                        Toast.makeText(context, "Phone radio is off", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        // Receiver for SMS delivered status
        deliveredReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case RESULT_OK:
                        Log.d(TAG, "SMS delivered successfully");
                        break;
                    default:
                        Log.e(TAG, "SMS not delivered");
                        break;
                }
            }
        };

        // Register receivers using ContextCompat for compatibility
        ContextCompat.registerReceiver(this, sentReceiver, new IntentFilter(SMS_SENT), ContextCompat.RECEIVER_NOT_EXPORTED);
        ContextCompat.registerReceiver(this, deliveredReceiver, new IntentFilter(SMS_DELIVERED), ContextCompat.RECEIVER_NOT_EXPORTED);
    }

    private void makeEmergencyCall() {
        EmergencyContact primaryContact = dataManager.getPrimaryContact();

        if (primaryContact == null) {
            Toast.makeText(this, "Please add an emergency contact first", Toast.LENGTH_LONG).show();
            openManageContacts();
            return;
        }

        // Check both CALL and SMS permissions
        boolean hasCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;

        if (!hasCallPermission || !hasSmsPermission) {
            isEmergencyCallPending = true;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS},
                    REQUEST_CALL_AND_SMS_PERMISSION);
        } else {
            // Play auto-message and then call
            playEmergencyMessageAndCall(primaryContact.getPhone());
            sendEmergencySmsToAllContacts();
        }
    }

    private void playEmergencyMessageAndCall(String phoneNumber) {
        if (!isTtsReady) {
            // TTS not ready yet, save number and wait
            pendingPhoneNumber = phoneNumber;
            Toast.makeText(this, "Preparing emergency call...", Toast.LENGTH_SHORT).show();
            return;
        }

        String patientId = dataManager.getPatientId();
        PersonalInfo personalInfo = dataManager.getPersonalInfo();

        // Build emergency message
        String message = "Emergency alert! This is an automated message. " +
                "Patient I.D. " + formatPatientIdForSpeech(patientId) + ". " +
                "Patient name: " + personalInfo.getName() + ". " +
                "Blood group: " + personalInfo.getBloodGroup() + ". " +
                "Please access the emergency medical portal and enter the patient I.D. to view complete medical information. " +
                "Patient I.D. again: " + formatPatientIdForSpeech(patientId);

        // Make call first, then speak (so they hear it!)
        callNumber(phoneNumber);

        // Wait 3 seconds for call to connect, then speak
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Turn on speaker phone
                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                    audioManager.setSpeakerphoneOn(true);
                }

                // Speak the message - emergency contact will hear it!
                Bundle params = new Bundle();
                params.putFloat(TextToSpeech.Engine.KEY_PARAM_VOLUME, 1.0f);
                textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, params, "emergencyMessage");

                Toast.makeText(MainActivity.this, "Speaking Patient ID to emergency contact...", Toast.LENGTH_LONG).show();
            }
        }, 3000); // 3 second delay for call to connect
    }

    private String formatPatientIdForSpeech(String patientId) {
        // Format "EMG-A3F9-2B4D-8C1E" to be spoken clearly
        // Convert to: "E M G, A 3 F 9, 2 B 4 D, 8 C 1 E"
        if (patientId == null) return "";

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < patientId.length(); i++) {
            char c = patientId.charAt(i);
            if (c == '-') {
                formatted.append(", ");
            } else {
                formatted.append(c).append(" ");
            }
        }
        return formatted.toString().trim();
    }

    private void callNumber(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        try {
            startActivity(callIntent);
        } catch (SecurityException e) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareDetailsWithContacts() {
        List<EmergencyContact> contacts = dataManager.getEmergencyContacts();

        if (contacts.isEmpty()) {
            Toast.makeText(this, "Please add emergency contacts first", Toast.LENGTH_LONG).show();
            openManageContacts();
            return;
        }

        // Check SMS permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            isEmergencyCallPending = false;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    REQUEST_SMS_PERMISSION);
        } else {
            // Refresh location before sending SMS
            fetchCurrentLocation();
            sendEmergencySmsToAllContacts();
        }
    }

    /**
     * Sends emergency medical details via SMS to all emergency contacts
     */
    private void sendEmergencySmsToAllContacts() {
        Log.d(TAG, "sendEmergencySmsToAllContacts called");

        List<EmergencyContact> contacts = dataManager.getEmergencyContacts();

        if (contacts.isEmpty()) {
            Log.w(TAG, "No emergency contacts available");
            Toast.makeText(this, "No emergency contacts available", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Found " + contacts.size() + " contacts");

        String message = buildMedicalDetailsMessage();
        Log.d(TAG, "Message length: " + message.length());
        Log.d(TAG, "Message: " + message);

        // SMS has a character limit, so we need to split long messages
        SmsManager smsManager = SmsManager.getDefault();
        int successCount = 0;
        int failCount = 0;

        for (EmergencyContact contact : contacts) {
            String phoneNumber = contact.getPhone();
            Log.d(TAG, "Sending SMS to: " + phoneNumber + " (" + contact.getName() + ")");

            try {
                // Create pending intents for tracking
                PendingIntent sentPI = PendingIntent.getBroadcast(
                        this,
                        0,
                        new Intent(SMS_SENT),
                        PendingIntent.FLAG_IMMUTABLE
                );

                PendingIntent deliveredPI = PendingIntent.getBroadcast(
                        this,
                        0,
                        new Intent(SMS_DELIVERED),
                        PendingIntent.FLAG_IMMUTABLE
                );

                // Split message if it's too long (SMS limit is 160 characters)
                ArrayList<String> messageParts = smsManager.divideMessage(message);
                Log.d(TAG, "Message split into " + messageParts.size() + " parts");

                if (messageParts.size() > 1) {
                    // Create ArrayLists for sent and delivered intents
                    ArrayList<PendingIntent> sentIntents = new ArrayList<>();
                    ArrayList<PendingIntent> deliveredIntents = new ArrayList<>();

                    for (int i = 0; i < messageParts.size(); i++) {
                        sentIntents.add(sentPI);
                        deliveredIntents.add(deliveredPI);
                    }

                    // Send multipart SMS
                    smsManager.sendMultipartTextMessage(
                            phoneNumber,
                            null,
                            messageParts,
                            sentIntents,
                            deliveredIntents
                    );
                    Log.d(TAG, "Multipart SMS sent to " + phoneNumber);
                } else {
                    // Send single SMS
                    smsManager.sendTextMessage(
                            phoneNumber,
                            null,
                            message,
                            sentPI,
                            deliveredPI
                    );
                    Log.d(TAG, "Single SMS sent to " + phoneNumber);
                }
                successCount++;

            } catch (Exception e) {
                failCount++;
                Log.e(TAG, "Failed to send SMS to " + phoneNumber + ": " + e.getMessage(), e);
                Toast.makeText(this, "Error sending to " + contact.getName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        // Show result to user
        Log.d(TAG, "SMS sending complete. Success: " + successCount + ", Failed: " + failCount);

        if (successCount > 0) {
            Toast.makeText(this,
                    "SMS sent to " + successCount + " contact(s). Check they receive it.",
                    Toast.LENGTH_LONG).show();
        }

        if (failCount > 0) {
            Toast.makeText(this,
                    "Failed to send to " + failCount + " contact(s)",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private String buildMedicalDetailsMessage() {
        PersonalInfo personalInfo = dataManager.getPersonalInfo();
        MedicalInfo medicalInfo = dataManager.getMedicalInfo();

        StringBuilder message = new StringBuilder();
        message.append("🚨 EMERGENCY MEDICAL INFORMATION 🚨\n\n");

        message.append("PERSONAL DETAILS:\n");
        message.append("Name: ").append(personalInfo.getName()).append("\n");
        message.append("Age: ").append(personalInfo.getAge()).append("\n");
        message.append("Blood Group: ").append(personalInfo.getBloodGroup()).append("\n");
        message.append("Address: ").append(personalInfo.getAddress()).append("\n\n");

        // Add GPS location if available
        if (currentLocation != null) {
            double lat = currentLocation.getLatitude();
            double lng = currentLocation.getLongitude();
            message.append("CURRENT LOCATION:\n");
            message.append("Latitude: ").append(lat).append("\n");
            message.append("Longitude: ").append(lng).append("\n");
            message.append("Google Maps: https://maps.google.com/?q=").append(lat).append(",").append(lng).append("\n\n");
        } else {
            message.append("LOCATION: Unable to fetch\n\n");
        }

        message.append("MEDICAL INFORMATION:\n");
        message.append("Allergies: ").append(medicalInfo.getAllergies().isEmpty() ? "None" : medicalInfo.getAllergies()).append("\n");
        message.append("Current Medications: ").append(medicalInfo.getMedications().isEmpty() ? "None" : medicalInfo.getMedications()).append("\n");
        message.append("Chronic Conditions: ").append(medicalInfo.getChronicConditions().isEmpty() ? "None" : medicalInfo.getChronicConditions()).append("\n\n");

        message.append("DOCTOR INFORMATION:\n");
        message.append("Doctor: ").append(medicalInfo.getDoctorName()).append("\n");
        message.append("Phone: ").append(medicalInfo.getDoctorPhone()).append("\n");

        return message.toString();
    }

    private void openEditProfile() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void openManageContacts() {
        Intent intent = new Intent(this, ManageContactsActivity.class);
        startActivity(intent);
    }

    /**
     * Request location permission and fetch current location
     */
    private void requestLocationPermissionAndFetch() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            fetchCurrentLocation();
        }
    }

    /**
     * Fetch current GPS location
     */
    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Try to get last known location first
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        Log.d(TAG, "Location fetched: " + location.getLatitude() + ", " + location.getLongitude());
                        Toast.makeText(this, "Location updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.w(TAG, "Last location is null, requesting fresh location");
                        Toast.makeText(this, "Getting your location...", Toast.LENGTH_SHORT).show();
                        // Request fresh location update
                        requestFreshLocation();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to get location", e);
                    Toast.makeText(this, "Failed to get location. Enable GPS.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Request fresh location update
     */
    private void requestFreshLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest =
                new LocationRequest.Builder(
                        Priority.PRIORITY_HIGH_ACCURACY, 5000)
                        .setMaxUpdates(1)
                        .build();

        fusedLocationClient.requestLocationUpdates(locationRequest,
                new com.google.android.gms.location.LocationCallback() {
                    @Override
                    public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
                        if (locationResult != null && locationResult.getLastLocation() != null) {
                            currentLocation = locationResult.getLastLocation();
                            Log.d(TAG, "Fresh location obtained: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
                            Toast.makeText(MainActivity.this, "Location acquired!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, null);
    }

    /**
     * Find nearest hospitals using Google Maps
     */
    private void findNearestHospitals() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show();
            requestLocationPermissionAndFetch();
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Open Google Maps with hospital search
                        String uri = "geo:" + latitude + "," + longitude + "?q=hospital";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        intent.setPackage("com.google.android.apps.maps");

                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            // If Google Maps not installed, use browser
                            String url = "https://www.google.com/maps/search/hospital/@" + latitude + "," + longitude + ",15z";
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                        }
                    } else {
                        Toast.makeText(this, "Unable to get location. Please enable GPS.", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to get location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                EmergencyContact primaryContact = dataManager.getPrimaryContact();
                if (primaryContact != null) {
                    callNumber(primaryContact.getPhone());
                }
            } else {
                Toast.makeText(this, "Call permission is required for emergency calls", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendEmergencySmsToAllContacts();
            } else {
                Toast.makeText(this, "SMS permission is required to send emergency details", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CALL_AND_SMS_PERMISSION) {
            boolean callGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            boolean smsGranted = grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED;

            if (isEmergencyCallPending) {
                EmergencyContact primaryContact = dataManager.getPrimaryContact();
                if (primaryContact != null) {
                    if (callGranted) {
                        playEmergencyMessageAndCall(primaryContact.getPhone());
                    } else {
                        Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
                    }

                    if (smsGranted) {
                        sendEmergencySmsToAllContacts();
                    } else {
                        Toast.makeText(this, "SMS permission denied - details not sent", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation();
                Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission denied - location won't be shared", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(sentReceiver);
            unregisterReceiver(deliveredReceiver);
        } catch (Exception e) {
            Log.e(TAG, "Error unregistering receivers", e);
        }

        // Cleanup text-to-speech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setupTextToSpeech() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e(TAG, "TTS language not supported");
                        isTtsReady = false;
                    } else {
                        isTtsReady = true;
                        Log.d(TAG, "TTS initialized successfully");

                        // If there's a pending call, make it now
                        if (pendingPhoneNumber != null) {
                            playEmergencyMessageAndCall(pendingPhoneNumber);
                            pendingPhoneNumber = null;
                        }
                    }
                } else {
                    Log.e(TAG, "TTS initialization failed");
                    isTtsReady = false;
                }
            }
        });
    }

    private void startAccidentCountdown() {
        if (isAccidentMode) {
            Toast.makeText(this, "Accident mode already active!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if emergency contact exists
        EmergencyContact primaryContact = dataManager.getPrimaryContact();
        if (primaryContact == null) {
            Toast.makeText(this, "Please add an emergency contact first!", Toast.LENGTH_LONG).show();
            openManageContacts();
            return;
        }

        isAccidentMode = true;

        // Play continuous siren (repeating beep)
        final Handler sirenHandler = new Handler();
        final Runnable sirenRunnable = new Runnable() {
            @Override
            public void run() {
                if (isAccidentMode && siren != null) {
                    siren.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300);
                    sirenHandler.postDelayed(this, 800); // Beep every 800ms
                }
            }
        };
        sirenHandler.post(sirenRunnable);

        // Show countdown dialog with big cancel button
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🚨 ACCIDENT DETECTED!")
                .setMessage("Sending emergency alert in 10 seconds...\n\nPress \"I'M OKAY\" to cancel")
                .setPositiveButton("I'M OKAY - CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User is okay, cancel everything
                        if (accidentCountdown != null) {
                            accidentCountdown.cancel();
                        }
                        isAccidentMode = false;
                        siren.stopTone();
                        sirenHandler.removeCallbacks(sirenRunnable);
                        Toast.makeText(MainActivity.this, "✅ Emergency cancelled - You're safe!", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);

        countdownDialog = builder.create();
        countdownDialog.show();

        // Make the "I'M OKAY" button bigger and more prominent
        countdownDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(18);
        countdownDialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(true);

        // Start countdown timer
        accidentCountdown = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsLeft = millisUntilFinished / 1000;
                countdownDialog.setMessage(
                        "🚨 EMERGENCY ALERT 🚨\n\n" +
                                "Calling emergency contact in " + secondsLeft + " seconds...\n\n" +
                                "Patient ID: " + dataManager.getPatientId() + "\n\n" +
                                "Press \"I'M OKAY\" if you're fine!"
                );
            }

            @Override
            public void onFinish() {
                // Countdown finished - trigger emergency
                isAccidentMode = false;
                siren.stopTone();
                sirenHandler.removeCallbacks(sirenRunnable);

                if (countdownDialog != null && countdownDialog.isShowing()) {
                    countdownDialog.dismiss();
                }

                Toast.makeText(MainActivity.this, "🚨 EMERGENCY MODE ACTIVATED!", Toast.LENGTH_LONG).show();

                // Trigger emergency procedures
                triggerEmergencyMode();
            }
        }.start();
    }

    private void triggerEmergencyMode() {
        Log.d(TAG, "triggerEmergencyMode called");

        // Check permissions
        boolean hasCallPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED;
        boolean hasSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;

        if (!hasCallPermission || !hasSmsPermission) {
            isEmergencyCallPending = true;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS},
                    REQUEST_CALL_AND_SMS_PERMISSION);
            return;
        }

        // Get primary emergency contact
        EmergencyContact primaryContact = dataManager.getPrimaryContact();
        if (primaryContact == null) {
            Toast.makeText(this, "No emergency contact found!", Toast.LENGTH_LONG).show();
            return;
        }

        // Update Firebase emergency status
        dataManager.updateEmergencyStatus(true);

        // Update GPS location in Firebase
        if (currentLocation != null) {
            dataManager.updateLocation(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        // 1. Send SMS to all emergency contacts
        sendEmergencySmsToAllContacts();

        // 2. Make auto-call with TTS message to primary contact
        playEmergencyMessageAndCall(primaryContact.getPhone());

        Toast.makeText(this,
                "Emergency alert sent!\nCalling " + primaryContact.getName() + "...",
                Toast.LENGTH_LONG).show();
    }

    private void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void openHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    private void openAlerts() {
        Intent intent = new Intent(this, AccidentAlertActivity.class);
        startActivity(intent);
    }

    private void updateLocationStatusText(boolean isAutoLocationEnabled) {
        if (tvLocationStatus == null) {
            return;
        }

        if (isAutoLocationEnabled) {
            tvLocationStatus.setText(R.string.location_gps_active);
        } else {
            tvLocationStatus.setText(R.string.location_gps_inactive);
        }
    }

    /**
     * Premium UI Animations: Breathing avatar, pulse detection, slide-in cards.
     */
    private void startPremiumAnimations() {
        // 1. Breathing animation for profile avatar
        if (ivProfilePhoto != null) {
            Animation breathingAnim = AnimationUtils.loadAnimation(this, R.anim.breathing_animation);
            ivProfilePhoto.startAnimation(breathingAnim);
        }

        // 2. Pulse animation for detection status icon
        if (ivDetectionIcon != null) {
            Animation pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse_animation);
            ivDetectionIcon.startAnimation(pulseAnim);
        }

        // 3. Slide-in animation for cards with stagger effect
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cardLocationStatus != null) {
                    Animation slideIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_up);
                    cardLocationStatus.startAnimation(slideIn);
                }
            }
        }, 100);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cardEmergencyContacts != null) {
                    Animation slideIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_up);
                    cardEmergencyContacts.startAnimation(slideIn);
                }
            }
        }, 200);
    }
}
