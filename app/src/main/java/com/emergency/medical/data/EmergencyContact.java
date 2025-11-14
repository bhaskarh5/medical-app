package com.emergency.medical.data;

public class EmergencyContact {
    private String id;
    private String name;
    private String phone;
    private boolean isPrimary;

    public EmergencyContact() {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = "";
        this.phone = "";
        this.isPrimary = false;
    }

    public EmergencyContact(String name, String phone, boolean isPrimary) {
        this.id = String.valueOf(System.currentTimeMillis());
        this.name = name;
        this.phone = phone;
        this.isPrimary = isPrimary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
}
