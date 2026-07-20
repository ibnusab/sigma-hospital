package com.sigma.hospital.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.models.Appointment;
import com.sigma.hospital.ui.adapters.AppointmentAdapter;
import com.sigma.hospital.viewmodel.HospitalViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentFragment extends Fragment {

    private HospitalViewModel viewModel;
    private AppointmentAdapter adapter;

    private TextInputEditText patientInput;
    private TextInputEditText doctorInput;
    private TextInputEditText dateInput;
    private TextInputEditText timeInput;
    private TextInputEditText notesInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Book Appointments");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        patientInput = view.findViewById(R.id.appt_patient_input);
        doctorInput = view.findViewById(R.id.appt_doctor_input);
        dateInput = view.findViewById(R.id.appt_date_input);
        timeInput = view.findViewById(R.id.appt_time_input);
        notesInput = view.findViewById(R.id.appt_notes_input);
        MaterialButton btnBook = view.findViewById(R.id.btn_save_appointment);
        RecyclerView recyclerView = view.findViewById(R.id.rv_appointments);

        // Autofill doctor name if prefilled argument exists
        Bundle args = getArguments();
        if (args != null && args.containsKey("prefilled_doctor_name")) {
            String prefilled = args.getString("prefilled_doctor_name");
            if (doctorInput != null) {
                doctorInput.setText(prefilled);
            }
        }

        // Autofill Patient's name for logged-in Patient role
        if ("Patient".equals(MainActivity.userRole)) {
            if (patientInput != null) {
                patientInput.setText("Jane Doe");
                patientInput.setEnabled(false);
            }
        }

        // Pre-fill default dates for usability
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateInput.setText(currentDate);
        timeInput.setText("10:00 AM");

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AppointmentAdapter(appt -> {
            appt.setPaymentStatus("PAID");
            viewModel.updateAppointment(appt);
            Toast.makeText(getContext(), "Billing Payment of $150.00 processed successfully! Fully PAID.", Toast.LENGTH_LONG).show();
        });
        recyclerView.setAdapter(adapter);

        btnBook.setOnClickListener(v -> saveAppointment());

        viewModel.getAllAppointments().observe(getViewLifecycleOwner(), appts -> {
            if (appts != null) {
                adapter.setAppointments(appts);
            }
        });
    }

    private void saveAppointment() {
        String patient = patientInput.getText() != null ? patientInput.getText().toString().trim() : "";
        String doctor = doctorInput.getText() != null ? doctorInput.getText().toString().trim() : "";
        String date = dateInput.getText() != null ? dateInput.getText().toString().trim() : "";
        String time = timeInput.getText() != null ? timeInput.getText().toString().trim() : "";
        String notes = notesInput.getText() != null ? notesInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(patient)) {
            patientInput.setError("Patient name is required");
            return;
        }

        if (TextUtils.isEmpty(doctor)) {
            doctorInput.setError("Doctor name is required");
            return;
        }

        Appointment appt = new Appointment();
        appt.setPatientName(patient);
        appt.setDoctorName(doctor);
        appt.setDate(date);
        appt.setTime(time);
        appt.setNotes(notes);
        appt.setStatus("SCHEDULED");

        viewModel.insertAppointment(appt);
        Toast.makeText(getContext(), "Appointment scheduled successfully!", Toast.LENGTH_SHORT).show();

        // Clear fields
        patientInput.setText("");
        doctorInput.setText("");
        notesInput.setText("");
    }
}
