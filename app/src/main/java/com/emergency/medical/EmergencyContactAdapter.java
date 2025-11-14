package com.emergency.medical;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.emergency.medical.data.EmergencyContact;

import java.util.List;

public class EmergencyContactAdapter extends RecyclerView.Adapter<EmergencyContactAdapter.ContactViewHolder> {

    private List<EmergencyContact> contacts;
    private OnContactActionListener listener;

    public interface OnContactActionListener {
        void onEditContact(EmergencyContact contact);

        void onDeleteContact(EmergencyContact contact);
    }

    public EmergencyContactAdapter(List<EmergencyContact> contacts, OnContactActionListener listener) {
        this.contacts = contacts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        EmergencyContact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updateContacts(List<EmergencyContact> newContacts) {
        this.contacts = newContacts;
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        TextView primaryBadge;
        ImageButton editButton;
        ImageButton deleteButton;

        ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contactName);
            phoneTextView = itemView.findViewById(R.id.contactPhone);
            primaryBadge = itemView.findViewById(R.id.primaryBadge);
            editButton = itemView.findViewById(R.id.btnEditContact);
            deleteButton = itemView.findViewById(R.id.btnDeleteContact);
        }

        void bind(EmergencyContact contact) {
            nameTextView.setText(contact.getName());
            phoneTextView.setText(contact.getPhone());
            primaryBadge.setVisibility(contact.isPrimary() ? View.VISIBLE : View.GONE);

            editButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditContact(contact);
                }
            });

            deleteButton.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteContact(contact);
                }
            });
        }
    }
}
