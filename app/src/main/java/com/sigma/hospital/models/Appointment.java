package com.sigma.hospital.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "appointments")
public class Appointment implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int doctorId;
    private String doctorName;
    private int patientId;
    private String patientName;
    private String date; // YYYY-MM-DD
    private String time; // HH:MM
    private String status; // Scheduled, Rescheduled, Completed, Cancelled
    private String notes;
    private String paymentStatus; // PENDING or PAID

    public Appointment() {
        this.status = "Scheduled";
        this.paymentStatus = "PENDING";
    }

    @androidx.room.Ignore
    public Appointment(int doctorId, String doctorName, int patientId, String patientName, 
                       String date, String time, String status, String notes) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.date = date;
        this.time = time;
        this.status = status;
        this.notes = notes;
        this.paymentStatus = "PENDING";
    }

    // Getters & Setters
    public String getPaymentStatus() {
        if (paymentStatus == null) {
            paymentStatus = "PENDING";
        }
        return paymentStatus;
    }
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
