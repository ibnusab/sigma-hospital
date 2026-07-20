package com.sigma.hospital.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "patients")
public class Patient implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String photoUri;
    private String medicalRecordNumber;
    private String nationalId;
    private String name;
    private String gender;
    private String dateOfBirth;
    private String bloodType;
    private String phone;
    private String email;
    private String address;
    private String emergencyContact;
    private String insurance;
    private String medicalHistory;

    // Constructors
    public Patient() {}

    @androidx.room.Ignore
    public Patient(String photoUri, String medicalRecordNumber, String nationalId, String name, 
                   String gender, String dateOfBirth, String bloodType, String phone, String email, 
                   String address, String emergencyContact, String insurance, String medicalHistory) {
        this.photoUri = photoUri;
        this.medicalRecordNumber = medicalRecordNumber;
        this.nationalId = nationalId;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bloodType = bloodType;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.emergencyContact = emergencyContact;
        this.insurance = insurance;
        this.medicalHistory = medicalHistory;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }

    public String getMedicalRecordNumber() { return medicalRecordNumber; }
    public void setMedicalRecordNumber(String medicalRecordNumber) { this.medicalRecordNumber = medicalRecordNumber; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getInsurance() { return insurance; }
    public void setInsurance(String insurance) { this.insurance = insurance; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }
}
