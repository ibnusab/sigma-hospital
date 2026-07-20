package com.sigma.hospital.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notifications")
public class NotificationAlert implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String message;
    private String timestamp; // YYYY-MM-DD HH:MM
    private String type; // Appointment, Medicine, Announcement
    private boolean isRead;

    public NotificationAlert() {
        this.isRead = false;
    }

    @androidx.room.Ignore
    public NotificationAlert(String title, String message, String timestamp, String type) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.isRead = false;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isRead() { return isRead; }
    public void setRead(boolean read) { isRead = read; }
}
