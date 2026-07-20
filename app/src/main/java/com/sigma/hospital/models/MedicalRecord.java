package com.sigma.hospital.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "medical_records")
public class MedicalRecord implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int patientId;
    private String patientName;
    private String patientMrn; // Medical Record Number
    private int doctorId;
    private String doctorName;
    private String visitDate;
    private String symptoms;
    private String physicalExamination;
    private String diagnosisNotes;
    private String prescription;
    private String laboratoryResult;
    
    // Vitals
    private String height; // in cm
    private String weight; // in kg
    private String bloodPressure; // e.g. "120/80"
    private String temperature; // in C/F
    private String heartRate; // bpm
    private String respiratoryRate; // breaths/min
    private String attachmentPath;

    public MedicalRecord() {}

    @androidx.room.Ignore
    public MedicalRecord(int patientId, String patientName, String patientMrn, int doctorId, String doctorName, 
                         String visitDate, String symptoms, String physicalExamination, String diagnosisNotes, 
                         String prescription, String laboratoryResult, String height, String weight, 
                         String bloodPressure, String temperature, String heartRate, String respiratoryRate, 
                         String attachmentPath) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientMrn = patientMrn;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.visitDate = visitDate;
        this.symptoms = symptoms;
        this.physicalExamination = physicalExamination;
        this.diagnosisNotes = diagnosisNotes;
        this.prescription = prescription;
        this.laboratoryResult = laboratoryResult;
        this.height = height;
        this.weight = weight;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.attachmentPath = attachmentPath;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getPatientMrn() { return patientMrn; }
    public void setPatientMrn(String patientMrn) { this.patientMrn = patientMrn; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getVisitDate() { return visitDate; }
    public void setVisitDate(String visitDate) { this.visitDate = visitDate; }

    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }

    public String getPhysicalExamination() { return physicalExamination; }
    public void setPhysicalExamination(String physicalExamination) { this.physicalExamination = physicalExamination; }

    public String getDiagnosisNotes() { return diagnosisNotes; }
    public void setDiagnosisNotes(String diagnosisNotes) { this.diagnosisNotes = diagnosisNotes; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public String getLaboratoryResult() { return laboratoryResult; }
    public void setLaboratoryResult(String laboratoryResult) { this.laboratoryResult = laboratoryResult; }

    public String getHeight() { return height; }
    public void setHeight(String height) { this.height = height; }

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }

    public String getBloodPressure() { return bloodPressure; }
    public void setBloodPressure(String bloodPressure) { this.bloodPressure = bloodPressure; }

    public String getTemperature() { return temperature; }
    public void setTemperature(String temperature) { this.temperature = temperature; }

    public String getHeartRate() { return heartRate; }
    public void setHeartRate(String heartRate) { this.heartRate = heartRate; }

    public String getRespiratoryRate() { return respiratoryRate; }
    public void setRespiratoryRate(String respiratoryRate) { this.respiratoryRate = respiratoryRate; }

    public String getAttachmentPath() { return attachmentPath; }
    public void setAttachmentPath(String attachmentPath) { this.attachmentPath = attachmentPath; }
}
