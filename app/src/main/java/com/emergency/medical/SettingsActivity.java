package com.emergency.medical;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.emergency.medical.data.SettingsManager;

public class SettingsActivity extends AppCompatActivity {

    private SettingsManager settingsManager;

    private ImageButton btnBack;
    private SwitchCompat switchDetection;
    private SwitchCompat switchVoice;
    private SwitchCompat switchLocation;
    private LinearLayout btnManageContacts;
    private LinearLayout btnUpdateMedical;
    private LinearLayout btnClearHistory;
    private LinearLayout btnAbout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsManager = new SettingsManager(this);

        initializeViews();
        loadSettings();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        switchDetection = findViewById(R.id.switchDetection);
        switchVoice = findViewById(R.id.switchVoice);
        switchLocation = findViewById(R.id.switchLocation);
        btnManageContacts = findViewById(R.id.btnManageContacts);
        btnUpdateMedical = findViewById(R.id.btnUpdateMedical);
        btnClearHistory = findViewById(R.id.btnClearHistory);
        btnAbout = findViewById(R.id.btnAbout);
    }

    private void loadSettings() {
        switchDetection.setChecked(settingsManager.isAccidentDetectionEnabled());
        switchVoice.setChecked(settingsManager.isVoiceAlertsEnabled());
        switchLocation.setChecked(settingsManager.isAutoLocationEnabled());
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        switchDetection.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setAccidentDetectionEnabled(isChecked);
            Toast.makeText(this,
                    isChecked
                            ? R.string.settings_accident_detection_enabled
                            : R.string.settings_accident_detection_disabled,
                    Toast.LENGTH_SHORT).show();
        });

        switchVoice.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setVoiceAlertsEnabled(isChecked);
            Toast.makeText(this,
                    isChecked
                            ? R.string.settings_voice_alerts_enabled
                            : R.string.settings_voice_alerts_disabled,
                    Toast.LENGTH_SHORT).show();
        });

        switchLocation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            settingsManager.setAutoLocationEnabled(isChecked);
            Toast.makeText(this,
                    isChecked
                            ? R.string.settings_auto_location_enabled
                            : R.string.settings_auto_location_disabled,
                    Toast.LENGTH_SHORT).show();
        });

        btnManageContacts.setOnClickListener(v ->
                startActivity(new Intent(this, ManageContactsActivity.class)));

        btnUpdateMedical.setOnClickListener(v ->
                startActivity(new Intent(this, EditProfileActivity.class)));

        btnClearHistory.setOnClickListener(v -> showClearHistoryConfirmation());
        btnAbout.setOnClickListener(v -> showAboutDialog());
    }

    private void showClearHistoryConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_history)
                .setMessage(R.string.settings_clear_history_message)
                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                        Toast.makeText(this,
                                R.string.settings_clear_history_confirmation,
                                Toast.LENGTH_SHORT).show())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void showAboutDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.settings_about_title)
                .setMessage(R.string.settings_about_message)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
