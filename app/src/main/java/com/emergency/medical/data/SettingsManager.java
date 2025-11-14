package com.emergency.medical.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {

    private static final String PREFS_NAME = "EmergencyMedicalSettings";
    private static final String KEY_ACCIDENT_DETECTION_ENABLED = "accident_detection_enabled";
    private static final String KEY_VOICE_ALERTS_ENABLED = "voice_alerts_enabled";
    private static final String KEY_AUTO_LOCATION_ENABLED = "auto_location_enabled";

    private final SharedPreferences sharedPreferences;

    public SettingsManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean isAccidentDetectionEnabled() {
        return sharedPreferences.getBoolean(KEY_ACCIDENT_DETECTION_ENABLED, true);
    }

    public void setAccidentDetectionEnabled(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(KEY_ACCIDENT_DETECTION_ENABLED, isEnabled).apply();
    }

    public boolean isVoiceAlertsEnabled() {
        return sharedPreferences.getBoolean(KEY_VOICE_ALERTS_ENABLED, false);
    }

    public void setVoiceAlertsEnabled(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(KEY_VOICE_ALERTS_ENABLED, isEnabled).apply();
    }

    public boolean isAutoLocationEnabled() {
        return sharedPreferences.getBoolean(KEY_AUTO_LOCATION_ENABLED, true);
    }

    public void setAutoLocationEnabled(boolean isEnabled) {
        sharedPreferences.edit().putBoolean(KEY_AUTO_LOCATION_ENABLED, isEnabled).apply();
    }
}
