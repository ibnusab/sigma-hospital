package com.sigma.hospital.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.sigma.hospital.database.dao.HospitalDao;
import com.sigma.hospital.models.Doctor;
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.models.Appointment;
import com.sigma.hospital.models.DoctorSchedule;
import com.sigma.hospital.models.MedicalRecord;
import com.sigma.hospital.models.NotificationAlert;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Doctor.class, Patient.class, Appointment.class, DoctorSchedule.class, MedicalRecord.class, NotificationAlert.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract HospitalDao hospitalDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "sigma_hospital_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                HospitalDao dao = INSTANCE.hospitalDao();

                // Seed Doctors
                dao.insertDoctor(new Doctor("", "Dr. Sarah Jenkins", "Female", "Cardiology", "LIC-12345", "+15550192", "sarah.jenkins@sigma.com", "Mon-Wed: 09:00-15:00", true));
                dao.insertDoctor(new Doctor("", "Dr. Marcus Vance", "Male", "Pediatrics", "LIC-67890", "+15550231", "marcus.vance@sigma.com", "Tue-Thu: 10:00-16:00", true));
                dao.insertDoctor(new Doctor("", "Dr. Evelyn Ross", "Female", "Neurology", "LIC-45678", "+15550345", "evelyn.ross@sigma.com", "Wed-Fri: 08:00-14:00", true));
                dao.insertDoctor(new Doctor("", "Dr. David Kim", "Male", "Orthopedics", "LIC-98765", "+15550456", "david.kim@sigma.com", "Mon-Thu: 09:00-17:00", true));

                // Seed Patients
                dao.insertPatient(new Patient("", "MRN-2026-0001", "NID-7738221", "John Doe", "Male", "1985-04-12", "A+", "+15559871", "john.doe@email.com", "123 Maple St, Seattle", "Jane Doe (+15559872)", "Sigma Health Insurance", "No chronic conditions. Mild pollen allergies."));
                dao.insertPatient(new Patient("", "MRN-2026-0002", "NID-9921102", "Alice Smith", "Female", "1992-09-24", "O-", "+15558763", "alice.smith@email.com", "456 Oak Ave, Bellevue", "Bob Smith (+15558764)", "Aetna Medical Plans", "Asthma history. Prescribed albuterol inhaler."));
                dao.insertPatient(new Patient("", "MRN-2026-0003", "NID-1102234", "Robert Patterson", "Male", "1968-11-03", "B+", "+15551239", "robert.p@email.com", "789 Pine Rd, Tacoma", "Linda Patterson (+15551240)", "Blue Cross Association", "Hypertension. Taking lisinopril daily."));

                // Seed Appointments
                dao.insertAppointment(new Appointment(1, "Dr. Sarah Jenkins", 1, "John Doe", "2026-07-19", "09:30", "Scheduled", "Routine cardiac checkup"));
                dao.insertAppointment(new Appointment(2, "Dr. Marcus Vance", 2, "Alice Smith", "2026-07-19", "11:00", "Scheduled", "Pediatric consultation for allergy follow-up"));
                dao.insertAppointment(new Appointment(3, "Dr. Evelyn Ross", 3, "Robert Patterson", "2026-07-20", "14:00", "Scheduled", "Post-stroke neural evaluation"));

                // Seed DoctorSchedules
                dao.insertSchedule(new DoctorSchedule(1, "Dr. Sarah Jenkins", "2026-07-19", "09:00", "15:00", true));
                dao.insertSchedule(new DoctorSchedule(2, "Dr. Marcus Vance", "2026-07-19", "10:00", "16:00", true));
                dao.insertSchedule(new DoctorSchedule(3, "Dr. Evelyn Ross", "2026-07-20", "08:00", "14:00", true));

                // Seed Medical Records
                dao.insertMedicalRecord(new MedicalRecord(1, "John Doe", "MRN-2026-0001", 1, "Dr. Sarah Jenkins", "2026-07-12", "Chest tightness, fatigue", "Heart rate 72 bpm, Blood Pressure 122/80 mmHg, clear lungs.", "Normal sinus rhythm on ECG. Stress-induced fatigue.", "Coenzyme Q10 daily, hydration, reduce caffeine.", "ECG Report attached: Negative for ischemia.", "180", "78", "122/80", "36.6", "72", "16", ""));
                dao.insertMedicalRecord(new MedicalRecord(3, "Robert Patterson", "MRN-2026-0003", 3, "Dr. Evelyn Ross", "2026-07-10", "Mild headaches, occasional vertigo", "BP 138/92 mmHg, neurological motor functions fully intact.", "Early hypertension-related tension headaches.", "Lisinopril 10mg PO QD, monitor blood pressure.", "Blood lipids: LDL 110, HDL 45.", "175", "84", "138/92", "36.8", "68", "14", ""));

                // Seed Notifications
                dao.insertNotification(new NotificationAlert("New Appointment", "John Doe scheduled an appointment with Dr. Sarah Jenkins", "2026-07-19 08:30", "Appointment"));
                dao.insertNotification(new NotificationAlert("Inventory Alert", "Influenza vaccines are running low. Please reorder.", "2026-07-18 17:00", "Announcement"));
                dao.insertNotification(new NotificationAlert("Shift Started", "Dr. Sarah Jenkins clocked in at 08:55 AM", "2026-07-19 08:55", "Announcement"));
            });
        }
    };
}
