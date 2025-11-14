package com.emergency.medical;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.medical.data.DataManager;
import com.emergency.medical.data.EmergencyContact;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class ManageContactsActivity extends AppCompatActivity implements EmergencyContactAdapter.OnContactActionListener {

    private DataManager dataManager;
    private RecyclerView recyclerView;
    private EmergencyContactAdapter adapter;
    private MaterialButton btnAddContact;
    private MaterialToolbar toolbar;
    private List<EmergencyContact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_contacts);

        dataManager = new DataManager(this);
        initializeViews();
        loadContacts();
        setupListeners();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Emergency Contacts");
        }

        recyclerView = findViewById(R.id.recyclerViewContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnAddContact = findViewById(R.id.btnAddContact);
    }

    private void loadContacts() {
        contacts = dataManager.getEmergencyContacts();
        adapter = new EmergencyContactAdapter(contacts, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupListeners() {
        btnAddContact.setOnClickListener(v -> showAddContactDialog(null));

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void showAddContactDialog(EmergencyContact existingContact) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null);

        TextInputEditText etContactName = dialogView.findViewById(R.id.etContactName);
        TextInputEditText etContactPhone = dialogView.findViewById(R.id.etContactPhone);
        CheckBox cbPrimary = dialogView.findViewById(R.id.cbPrimary);

        if (existingContact != null) {
            etContactName.setText(existingContact.getName());
            etContactPhone.setText(existingContact.getPhone());
            cbPrimary.setChecked(existingContact.isPrimary());
        }

        builder.setView(dialogView)
                .setTitle(existingContact == null ? "Add Emergency Contact" : "Edit Emergency Contact")
                .setPositiveButton(existingContact == null ? "Add" : "Update", (dialog, which) -> {
                    String name = etContactName.getText().toString().trim();
                    String phone = etContactPhone.getText().toString().trim();
                    boolean isPrimary = cbPrimary.isChecked();

                    if (name.isEmpty() || phone.isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // If setting as primary, remove primary from other contacts
                    if (isPrimary) {
                        for (EmergencyContact contact : contacts) {
                            contact.setPrimary(false);
                        }
                    }

                    if (existingContact == null) {
                        EmergencyContact newContact = new EmergencyContact(name, phone, isPrimary);
                        dataManager.addEmergencyContact(newContact);
                    } else {
                        EmergencyContact updatedContact = new EmergencyContact(name, phone, isPrimary);
                        dataManager.updateEmergencyContact(existingContact.getId(), updatedContact);
                    }

                    loadContacts();
                    Toast.makeText(this, "Contact saved", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onEditContact(EmergencyContact contact) {
        showAddContactDialog(contact);
    }

    @Override
    public void onDeleteContact(EmergencyContact contact) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete " + contact.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dataManager.deleteEmergencyContact(contact.getId());
                    loadContacts();
                    Toast.makeText(this, "Contact deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
