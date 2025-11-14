package com.emergency.medical.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DataManager {
    private static final String TAG = "DataManager";
    private static final String PREFS_NAME = "EmergencyMedicalPrefs";
    private static final String KEY_PERSONAL_INFO = "personal_info";
    private static final String KEY_MEDICAL_INFO = "medical_info";
    private static final String KEY_EMERGENCY_CONTACTS = "emergency_contacts";
    private static final String KEY_PATIENT_ID = "patient_id";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final DatabaseReference firebaseDatabase;

    public DataManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        gson = new Gson();

        // Initialize Firebase
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("patients");

        // Generate patient ID on first use
        if (getPatientId() == null) {
            generateAndSavePatientId();
        }

        Log.d(TAG, "DataManager initialized for Patient ID: " + getPatientId());
    }

    // Patient ID methods
    public String getPatientId() {
        return sharedPreferences.getString(KEY_PATIENT_ID, null);
    }

    private void generateAndSavePatientId() {
        String patientId = generatePatientId();
        sharedPreferences.edit().putString(KEY_PATIENT_ID, patientId).apply();
        Log.d(TAG, "Generated new Patient ID: " + patientId);
    }

    private String generatePatientId() {
        // Generate unique ID in format: EMG-XXXX-XXXX-XXXX
        String uuid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
        return String.format("EMG-%s-%s-%s",
                uuid.substring(0, 4),
                uuid.substring(4, 8),
                uuid.substring(8, 12));
    }

    // Personal Info methods
    public void savePersonalInfo(PersonalInfo personalInfo) {
        String json = gson.toJson(personalInfo);
        sharedPreferences.edit().putString(KEY_PERSONAL_INFO, json).apply();
        Log.d(TAG, "Personal info saved");

        // Sync to Firebase
        syncToFirebase();
    }

    public PersonalInfo getPersonalInfo() {
        String json = sharedPreferences.getString(KEY_PERSONAL_INFO, null);
        if (json != null) {
            return gson.fromJson(json, PersonalInfo.class);
        }
        return new PersonalInfo();
    }

    // Medical Info methods
    public void saveMedicalInfo(MedicalInfo medicalInfo) {
        String json = gson.toJson(medicalInfo);
        sharedPreferences.edit().putString(KEY_MEDICAL_INFO, json).apply();
        Log.d(TAG, "Medical info saved");

        // Sync to Firebase
        syncToFirebase();
    }

    public MedicalInfo getMedicalInfo() {
        String json = sharedPreferences.getString(KEY_MEDICAL_INFO, null);
        if (json != null) {
            return gson.fromJson(json, MedicalInfo.class);
        }
        return new MedicalInfo();
    }

    // Emergency Contacts methods
    public void saveEmergencyContacts(List<EmergencyContact> contacts) {
        String json = gson.toJson(contacts);
        sharedPreferences.edit().putString(KEY_EMERGENCY_CONTACTS, json).apply();
        Log.d(TAG, "Emergency contacts saved");

        // Sync to Firebase
        syncToFirebase();
    }

    public List<EmergencyContact> getEmergencyContacts() {
        String json = sharedPreferences.getString(KEY_EMERGENCY_CONTACTS, null);
        if (json != null) {
            Type listType = new TypeToken<ArrayList<EmergencyContact>>() {
            }.getType();
            return gson.fromJson(json, listType);
        }
        return new ArrayList<>();
    }

    public void addEmergencyContact(EmergencyContact contact) {
        List<EmergencyContact> contacts = getEmergencyContacts();
        contacts.add(contact);
        saveEmergencyContacts(contacts);
    }

    public void updateEmergencyContact(String id, EmergencyContact updatedContact) {
        List<EmergencyContact> contacts = getEmergencyContacts();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId().equals(id)) {
                updatedContact.setId(id);
                contacts.set(i, updatedContact);
                break;
            }
        }
        saveEmergencyContacts(contacts);
    }

    public void deleteEmergencyContact(String id) {
        List<EmergencyContact> contacts = getEmergencyContacts();
        contacts.removeIf(contact -> contact.getId().equals(id));
        saveEmergencyContacts(contacts);
    }

    public EmergencyContact getPrimaryContact() {
        List<EmergencyContact> contacts = getEmergencyContacts();
        for (EmergencyContact contact : contacts) {
            if (contact.isPrimary()) {
                return contact;
            }
        }
        // If no primary contact, return the first one
        if (!contacts.isEmpty()) {
            return contacts.get(0);
        }
        return null;
    }

    // Firebase Sync Methods

    /**
     * Syncs all patient data to Firebase Realtime Database
     */
    public void syncToFirebase() {
        String patientId = getPatientId();
        if (patientId == null) {
            Log.e(TAG, "Cannot sync to Firebase: Patient ID is null");
            return;
        }

        try {
            // Create data structure for Firebase
            Map<String, Object> patientData = new HashMap<>();

            patientData.put("id", patientId);
            patientData.put("personalInfo", getPersonalInfo());
            patientData.put("medicalInfo", getMedicalInfo());
            patientData.put("emergencyContacts", getEmergencyContacts());
            patientData.put("lastUpdated", System.currentTimeMillis());
            patientData.put("emergencyActive", false);

            // Upload to Firebase
            firebaseDatabase.child(patientId).setValue(patientData)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Data synced to Firebase successfully!");
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Failed to sync to Firebase: " + e.getMessage());
                    });

        } catch (Exception e) {
            Log.e(TAG, "Error syncing to Firebase: " + e.getMessage());
        }
    }

    /**
     * Updates emergency status in Firebase
     */
    public void updateEmergencyStatus(boolean isActive) {
        String patientId = getPatientId();
        if (patientId == null) return;

        firebaseDatabase.child(patientId).child("emergencyActive").setValue(isActive)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Emergency status updated: " + isActive);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update emergency status: " + e.getMessage());
                });
    }

    /**
     * Updates GPS location in Firebase
     */
    public void updateLocation(double latitude, double longitude) {
        String patientId = getPatientId();
        if (patientId == null) return;

        Map<String, Object> location = new HashMap<>();
        location.put("latitude", latitude);
        location.put("longitude", longitude);
        location.put("timestamp", System.currentTimeMillis());

        firebaseDatabase.child(patientId).child("location").setValue(location)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Location updated in Firebase");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to update location: " + e.getMessage());
                });
    }
}
