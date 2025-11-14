package com.emergency.medical.data;

public class MedicalInfo {
    private String allergies;
    private String medications;
    private String chronicConditions;
    private String doctorName;
    private String doctorPhone;

    public MedicalInfo() {
        this.allergies = "";
        this.medications = "";
        this.chronicConditions = "";
        this.doctorName = "";
        this.doctorPhone = "";
    }

    public MedicalInfo(String allergies, String medications, String chronicConditions,
                       String doctorName, String doctorPhone) {
        this.allergies = allergies;
        this.medications = medications;
        this.chronicConditions = chronicConditions;
        this.doctorName = doctorName;
        this.doctorPhone = doctorPhone;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getChronicConditions() {
        return chronicConditions;
    }

    public void setChronicConditions(String chronicConditions) {
        this.chronicConditions = chronicConditions;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }
}
