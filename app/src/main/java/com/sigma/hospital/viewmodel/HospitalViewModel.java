package com.sigma.hospital.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sigma.hospital.models.Doctor;
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.models.Appointment;
import com.sigma.hospital.models.DoctorSchedule;
import com.sigma.hospital.models.MedicalRecord;
import com.sigma.hospital.models.NotificationAlert;
import com.sigma.hospital.repository.HospitalRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HospitalViewModel extends AndroidViewModel {

    private final HospitalRepository repository;
    private final LiveData<List<Doctor>> allDoctors;
    private final LiveData<List<Patient>> allPatients;
    private final LiveData<List<Appointment>> allAppointments;
    private final LiveData<List<DoctorSchedule>> allSchedules;
    private final LiveData<List<MedicalRecord>> allMedicalRecords;
    private final LiveData<List<NotificationAlert>> allNotifications;

    private final LiveData<Integer> doctorCount;
    private final LiveData<Integer> patientCount;
    private final LiveData<Integer> appointmentCountToday;
    private final LiveData<Integer> totalAppointmentCount;
    private final LiveData<Integer> medicalRecordCount;

    // Selected state sharing variables
    private Doctor selectedDoctor;
    private Patient selectedPatient;

    public HospitalViewModel(@NonNull Application application) {
        super(application);
        repository = new HospitalRepository(application);
        allDoctors = repository.getAllDoctors();
        allPatients = repository.getAllPatients();
        allAppointments = repository.getAllAppointments();
        allSchedules = repository.getAllSchedules();
        allMedicalRecords = repository.getAllMedicalRecords();
        allNotifications = repository.getAllNotifications();

        doctorCount = repository.getDoctorCount();
        patientCount = repository.getPatientCount();
        totalAppointmentCount = repository.getTotalAppointmentCount();
        medicalRecordCount = repository.getMedicalRecordCount();

        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        appointmentCountToday = repository.getAppointmentCountToday(todayDate);
    }

    // --- DOCTORS ---
    public LiveData<List<Doctor>> getAllDoctors() { return allDoctors; }
    public LiveData<Doctor> getDoctorById(int id) { return repository.getDoctorById(id); }
    public LiveData<Integer> getDoctorCount() { return doctorCount; }
    public LiveData<List<Doctor>> searchDoctors(String query) { return repository.searchDoctors(query); }
    public void insertDoctor(Doctor doctor) { repository.insertDoctor(doctor); }
    public void updateDoctor(Doctor doctor) { repository.updateDoctor(doctor); }
    public void deleteDoctor(Doctor doctor) { repository.deleteDoctor(doctor); }

    // --- PATIENTS ---
    public LiveData<List<Patient>> getAllPatients() { return allPatients; }
    public LiveData<Patient> getPatientById(int id) { return repository.getPatientById(id); }
    public LiveData<Integer> getPatientCount() { return patientCount; }
    public LiveData<List<Patient>> searchPatients(String query) { return repository.searchPatients(query); }
    public void insertPatient(Patient patient) { repository.insertPatient(patient); }
    public void updatePatient(Patient patient) { repository.updatePatient(patient); }
    public void deletePatient(Patient patient) { repository.deletePatient(patient); }

    // --- APPOINTMENTS ---
    public LiveData<List<Appointment>> getAllAppointments() { return allAppointments; }
    public LiveData<List<Appointment>> getAppointmentsByDate(String date) { return repository.getAppointmentsByDate(date); }
    public LiveData<Integer> getAppointmentCountToday() { return appointmentCountToday; }
    public LiveData<Integer> getTotalAppointmentCount() { return totalAppointmentCount; }
    public void insertAppointment(Appointment appointment) { repository.insertAppointment(appointment); }
    public void updateAppointment(Appointment appointment) { repository.updateAppointment(appointment); }
    public void deleteAppointment(Appointment appointment) { repository.deleteAppointment(appointment); }

    // --- SCHEDULES ---
    public LiveData<List<DoctorSchedule>> getAllSchedules() { return allSchedules; }
    public LiveData<List<DoctorSchedule>> getSchedulesByDoctor(int doctorId) { return repository.getSchedulesByDoctor(doctorId); }
    public void insertSchedule(DoctorSchedule schedule) { repository.insertSchedule(schedule); }
    public void updateSchedule(DoctorSchedule schedule) { repository.updateSchedule(schedule); }
    public void deleteSchedule(DoctorSchedule schedule) { repository.deleteSchedule(schedule); }

    // --- MEDICAL RECORDS ---
    public LiveData<List<MedicalRecord>> getAllMedicalRecords() { return allMedicalRecords; }
    public LiveData<List<MedicalRecord>> getMedicalRecordsByPatient(int patientId) { return repository.getMedicalRecordsByPatient(patientId); }
    public LiveData<Integer> getMedicalRecordCount() { return medicalRecordCount; }
    public void insertMedicalRecord(MedicalRecord record) { repository.insertMedicalRecord(record); }
    public void updateMedicalRecord(MedicalRecord record) { repository.updateMedicalRecord(record); }
    public void deleteMedicalRecord(MedicalRecord record) { repository.deleteMedicalRecord(record); }

    // --- NOTIFICATIONS ---
    public LiveData<List<NotificationAlert>> getAllNotifications() { return allNotifications; }
    public void insertNotification(NotificationAlert notification) { repository.insertNotification(notification); }
    public void markAllNotificationsAsRead() { repository.markAllAsRead(); }

    // --- SHARED SELECTION GETTERS/SETTERS ---
    public Doctor getSelectedDoctor() { return selectedDoctor; }
    public void setSelectedDoctor(Doctor doctor) { this.selectedDoctor = doctor; }

    public Patient getSelectedPatient() { return selectedPatient; }
    public void setSelectedPatient(Patient patient) { this.selectedPatient = patient; }
}
