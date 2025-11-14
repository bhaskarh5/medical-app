package com.emergency.medical.data;

public class PersonalInfo {
    private String name;
    private String age;
    private String bloodGroup;
    private String address;
    private String photoUri;

    public PersonalInfo() {
        this.name = "";
        this.age = "";
        this.bloodGroup = "";
        this.address = "";
        this.photoUri = "";
    }

    public PersonalInfo(String name, String age, String bloodGroup, String address, String photoUri) {
        this.name = name;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.address = address;
        this.photoUri = photoUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }
}
