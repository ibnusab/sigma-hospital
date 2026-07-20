package com.sigma.hospital.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "doctors")
public class Doctor implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String photoUri;
    private String name;
    private String gender;
    private String specialization;
    private String licenseNumber;
    private String phone;
    private String email;
    private String practiceSchedule; // e.g. "Mon-Fri: 09:00 - 15:00"
    private boolean isActive;

    // Constructors
    public Doctor() {
        this.isActive = true;
    }

    @androidx.room.Ignore
    public Doctor(String photoUri, String name, String gender, String specialization, 
                  String licenseNumber, String phone, String email, String practiceSchedule, boolean isActive) {
        this.photoUri = photoUri;
        this.name = name;
        this.gender = gender;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.email = email;
        this.practiceSchedule = practiceSchedule;
        this.isActive = isActive;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPracticeSchedule() { return practiceSchedule; }
    public void setPracticeSchedule(String practiceSchedule) { this.practiceSchedule = practiceSchedule; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
