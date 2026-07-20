package com.sigma.hospital.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "doctor_schedules")
public class DoctorSchedule implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int doctorId;
    private String doctorName;
    private String date; // YYYY-MM-DD
    private String startTime; // HH:MM
    private String endTime; // HH:MM
    private boolean isAvailable;

    public DoctorSchedule() {
        this.isAvailable = true;
    }

    @androidx.room.Ignore
    public DoctorSchedule(int doctorId, String doctorName, String date, String startTime, String endTime, boolean isAvailable) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
