package com.sigma.hospital.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;

import com.sigma.hospital.database.AppDatabase;
import com.sigma.hospital.database.dao.HospitalDao;
import com.sigma.hospital.models.Doctor;
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.models.Appointment;
import com.sigma.hospital.models.DoctorSchedule;
import com.sigma.hospital.models.MedicalRecord;
import com.sigma.hospital.models.NotificationAlert;

import java.util.List;

public class HospitalRepository {

    private final HospitalDao hospitalDao;
    private final LiveData<List<Doctor>> allDoctors;
    private final LiveData<List<Patient>> allPatients;
    private final LiveData<List<Appointment>> allAppointments;
    private final LiveData<List<DoctorSchedule>> allSchedules;
    private final LiveData<List<MedicalRecord>> allMedicalRecords;
    private final LiveData<List<NotificationAlert>> allNotifications;

    public HospitalRepository(Context context) {
        AppDatabase db = AppDatabase.getDatabase(context);
        hospitalDao = db.hospitalDao();
        allDoctors = hospitalDao.getAllDoctors();
        allPatients = hospitalDao.getAllPatients();
        allAppointments = hospitalDao.getAllAppointments();
        allSchedules = hospitalDao.getAllSchedules();
        allMedicalRecords = hospitalDao.getAllMedicalRecords();
        allNotifications = hospitalDao.getAllNotifications();
    }

    // --- Doctor Repository ---
    public LiveData<List<Doctor>> getAllDoctors() { return allDoctors; }
    public LiveData<Doctor> getDoctorById(int id) { return hospitalDao.getDoctorById(id); }
    public LiveData<Integer> getDoctorCount() { return hospitalDao.getDoctorCount(); }
    public LiveData<List<Doctor>> searchDoctors(String query) { return hospitalDao.searchDoctors("%" + query + "%"); }
    public void insertDoctor(Doctor doctor) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.insertDoctor(doctor));
    }
    public void updateDoctor(Doctor doctor) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.updateDoctor(doctor));
    }
    public void deleteDoctor(Doctor doctor) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.deleteDoctor(doctor));
    }

    // --- Patient Repository ---
    public LiveData<List<Patient>> getAllPatients() { return allPatients; }
    public LiveData<Patient> getPatientById(int id) { return hospitalDao.getPatientById(id); }
    public LiveData<Integer> getPatientCount() { return hospitalDao.getPatientCount(); }
    public LiveData<List<Patient>> searchPatients(String query) { return hospitalDao.searchPatients("%" + query + "%"); }
    public void insertPatient(Patient patient) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.insertPatient(patient));
    }
    public void updatePatient(Patient patient) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.updatePatient(patient));
    }
    public void deletePatient(Patient patient) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.deletePatient(patient));
    }

    // --- Appointment Repository ---
    public LiveData<List<Appointment>> getAllAppointments() { return allAppointments; }
    public LiveData<List<Appointment>> getAppointmentsByDate(String date) { return hospitalDao.getAppointmentsByDate(date); }
    public LiveData<Integer> getAppointmentCountToday(String date) { return hospitalDao.getAppointmentCountToday(date); }
    public LiveData<Integer> getTotalAppointmentCount() { return hospitalDao.getTotalAppointmentCount(); }
    public void insertAppointment(Appointment appointment) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.insertAppointment(appointment));
    }
    public void updateAppointment(Appointment appointment) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.updateAppointment(appointment));
    }
    public void deleteAppointment(Appointment appointment) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.deleteAppointment(appointment));
    }

    // --- Schedule Repository ---
    public LiveData<List<DoctorSchedule>> getAllSchedules() { return allSchedules; }
    public LiveData<List<DoctorSchedule>> getSchedulesByDoctor(int doctorId) { return hospitalDao.getSchedulesByDoctor(doctorId); }
    public void insertSchedule(DoctorSchedule schedule) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.insertSchedule(schedule));
    }
    public void updateSchedule(DoctorSchedule schedule) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.updateSchedule(schedule));
    }
    public void deleteSchedule(DoctorSchedule schedule) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.deleteSchedule(schedule));
    }

    // --- Medical Record Repository ---
    public LiveData<List<MedicalRecord>> getAllMedicalRecords() { return allMedicalRecords; }
    public LiveData<List<MedicalRecord>> getMedicalRecordsByPatient(int patientId) { return hospitalDao.getMedicalRecordsByPatient(patientId); }
    public LiveData<Integer> getMedicalRecordCount() { return hospitalDao.getMedicalRecordCount(); }
    public void insertMedicalRecord(MedicalRecord record) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.insertMedicalRecord(record));
    }
    public void updateMedicalRecord(MedicalRecord record) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.updateMedicalRecord(record));
    }
    public void deleteMedicalRecord(MedicalRecord record) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.deleteMedicalRecord(record));
    }

    // --- Notification Repository ---
    public LiveData<List<NotificationAlert>> getAllNotifications() { return allNotifications; }
    public void insertNotification(NotificationAlert notification) {
        AppDatabase.databaseWriteExecutor.execute(() -> hospitalDao.insertNotification(notification));
    }
    public void markAllAsRead() {
        AppDatabase.databaseWriteExecutor.execute(hospitalDao::markAllAsRead);
    }
}
