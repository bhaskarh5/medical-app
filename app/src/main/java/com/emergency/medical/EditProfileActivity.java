package com.emergency.medical;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.emergency.medical.data.DataManager;
import com.emergency.medical.data.MedicalInfo;
import com.emergency.medical.data.PersonalInfo;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 2;

    private DataManager dataManager;
    private TextInputEditText etName, etAge, etBloodGroup, etAddress;
    private TextInputEditText etAllergies, etMedications, etConditions, etDoctorName, etDoctorPhone;
    private ImageView ivProfilePhoto;
    private ImageButton btnBack;
    private MaterialButton btnSave, btnSelectPhoto;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        dataManager = new DataManager(this);
        initializeViews();
        loadData();
        setupListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);

        // Personal Info fields
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etBloodGroup = findViewById(R.id.etBloodGroup);
        etAddress = findViewById(R.id.etAddress);
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);

        // Medical Info fields
        etAllergies = findViewById(R.id.etAllergies);
        etMedications = findViewById(R.id.etMedications);
        etConditions = findViewById(R.id.etConditions);
        etDoctorName = findViewById(R.id.etDoctorName);
        etDoctorPhone = findViewById(R.id.etDoctorPhone);

        btnSave = findViewById(R.id.btnSave);
    }

    private void loadData() {
        PersonalInfo personalInfo = dataManager.getPersonalInfo();
        MedicalInfo medicalInfo = dataManager.getMedicalInfo();

        // Load personal info
        etName.setText(personalInfo.getName());
        etAge.setText(personalInfo.getAge());
        etBloodGroup.setText(personalInfo.getBloodGroup());
        etAddress.setText(personalInfo.getAddress());

        if (!personalInfo.getPhotoUri().isEmpty()) {
            try {
                selectedImageUri = Uri.parse(personalInfo.getPhotoUri());
                ivProfilePhoto.setImageURI(selectedImageUri);
            } catch (Exception e) {
                ivProfilePhoto.setImageResource(R.drawable.ic_person);
            }
        }

        // Load medical info
        etAllergies.setText(medicalInfo.getAllergies());
        etMedications.setText(medicalInfo.getMedications());
        etConditions.setText(medicalInfo.getChronicConditions());
        etDoctorName.setText(medicalInfo.getDoctorName());
        etDoctorPhone.setText(medicalInfo.getDoctorPhone());
    }

    private void setupListeners() {
        btnSelectPhoto.setOnClickListener(v -> selectPhoto());
        btnSave.setOnClickListener(v -> saveData());

        btnBack.setOnClickListener(v -> finish());
    }

    private void selectPhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            openImagePicker();
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveData() {
        // Save personal info
        PersonalInfo personalInfo = new PersonalInfo(
                etName.getText().toString().trim(),
                etAge.getText().toString().trim(),
                etBloodGroup.getText().toString().trim(),
                etAddress.getText().toString().trim(),
                selectedImageUri != null ? selectedImageUri.toString() : ""
        );
        dataManager.savePersonalInfo(personalInfo);

        // Save medical info
        MedicalInfo medicalInfo = new MedicalInfo(
                etAllergies.getText().toString().trim(),
                etMedications.getText().toString().trim(),
                etConditions.getText().toString().trim(),
                etDoctorName.getText().toString().trim(),
                etDoctorPhone.getText().toString().trim()
        );
        dataManager.saveMedicalInfo(medicalInfo);

        Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                ivProfilePhoto.setImageURI(selectedImageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Storage permission is required to select a photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
