package com.sigma.hospital.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.sigma.hospital.models.Doctor;
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.models.Appointment;
import com.sigma.hospital.models.DoctorSchedule;
import com.sigma.hospital.models.MedicalRecord;
import com.sigma.hospital.models.NotificationAlert;

import java.util.List;

@Dao
public interface HospitalDao {

    // --- DOCTOR DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDoctor(Doctor doctor);

    @Update
    void updateDoctor(Doctor doctor);

    @Delete
    void deleteDoctor(Doctor doctor);

    @Query("SELECT * FROM doctors ORDER BY name ASC")
    LiveData<List<Doctor>> getAllDoctors();

    @Query("SELECT * FROM doctors WHERE id = :id LIMIT 1")
    LiveData<Doctor> getDoctorById(int id);

    @Query("SELECT COUNT(*) FROM doctors")
    LiveData<Integer> getDoctorCount();

    @Query("SELECT * FROM doctors WHERE name LIKE :searchQuery OR specialization LIKE :searchQuery OR licenseNumber LIKE :searchQuery ORDER BY name ASC")
    LiveData<List<Doctor>> searchDoctors(String searchQuery);


    // --- PATIENT DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPatient(Patient patient);

    @Update
    void updatePatient(Patient patient);

    @Delete
    void deletePatient(Patient patient);

    @Query("SELECT * FROM patients ORDER BY name ASC")
    LiveData<List<Patient>> getAllPatients();

    @Query("SELECT * FROM patients WHERE id = :id LIMIT 1")
    LiveData<Patient> getPatientById(int id);

    @Query("SELECT COUNT(*) FROM patients")
    LiveData<Integer> getPatientCount();

    @Query("SELECT * FROM patients WHERE name LIKE :searchQuery OR medicalRecordNumber LIKE :searchQuery OR nationalId LIKE :searchQuery ORDER BY name ASC")
    LiveData<List<Patient>> searchPatients(String searchQuery);


    // --- APPOINTMENT DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAppointment(Appointment appointment);

    @Update
    void updateAppointment(Appointment appointment);

    @Delete
    void deleteAppointment(Appointment appointment);

    @Query("SELECT * FROM appointments ORDER BY date ASC, time ASC")
    LiveData<List<Appointment>> getAllAppointments();

    @Query("SELECT * FROM appointments WHERE date = :date ORDER BY time ASC")
    LiveData<List<Appointment>> getAppointmentsByDate(String date);

    @Query("SELECT COUNT(*) FROM appointments WHERE date = :date AND status != 'Cancelled'")
    LiveData<Integer> getAppointmentCountToday(String date);

    @Query("SELECT COUNT(*) FROM appointments")
    LiveData<Integer> getTotalAppointmentCount();


    // --- DOCTOR SCHEDULE DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertSchedule(DoctorSchedule schedule);

    @Update
    void updateSchedule(DoctorSchedule schedule);

    @Delete
    void deleteSchedule(DoctorSchedule schedule);

    @Query("SELECT * FROM doctor_schedules ORDER BY date ASC, startTime ASC")
    LiveData<List<DoctorSchedule>> getAllSchedules();

    @Query("SELECT * FROM doctor_schedules WHERE doctorId = :doctorId ORDER BY date ASC")
    LiveData<List<DoctorSchedule>> getSchedulesByDoctor(int doctorId);


    // --- MEDICAL RECORD DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertMedicalRecord(MedicalRecord record);

    @Update
    void updateMedicalRecord(MedicalRecord record);

    @Delete
    void deleteMedicalRecord(MedicalRecord record);

    @Query("SELECT * FROM medical_records ORDER BY visitDate DESC")
    LiveData<List<MedicalRecord>> getAllMedicalRecords();

    @Query("SELECT * FROM medical_records WHERE patientId = :patientId ORDER BY visitDate DESC")
    LiveData<List<MedicalRecord>> getMedicalRecordsByPatient(int patientId);

    @Query("SELECT COUNT(*) FROM medical_records")
    LiveData<Integer> getMedicalRecordCount();


    // --- NOTIFICATION ALERT DAO ---
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNotification(NotificationAlert notification);

    @Update
    void updateNotification(NotificationAlert notification);

    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    LiveData<List<NotificationAlert>> getAllNotifications();

    @Query("UPDATE notifications SET isRead = 1")
    void markAllAsRead();
}
