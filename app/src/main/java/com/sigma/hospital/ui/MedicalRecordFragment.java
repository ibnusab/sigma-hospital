package com.sigma.hospital.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.models.MedicalRecord;
import com.sigma.hospital.ui.adapters.MedicalRecordAdapter;
import com.sigma.hospital.utils.PdfExporter;
import com.sigma.hospital.viewmodel.HospitalViewModel;
import java.io.File;

public class MedicalRecordFragment extends Fragment implements MedicalRecordAdapter.OnRecordActionListener {

    private HospitalViewModel viewModel;
    private MedicalRecordAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyState;
    private TextInputEditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medical_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Medical Records");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        recyclerView = view.findViewById(R.id.rv_medical_records);
        emptyState = view.findViewById(R.id.records_empty_state);
        searchInput = view.findViewById(R.id.search_records_input);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MedicalRecordAdapter(this);
        recyclerView.setAdapter(adapter);

        // Observe records list
        viewModel.getAllMedicalRecords().observe(getViewLifecycleOwner(), records -> {
            if (records == null || records.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                adapter.setRecords(records);
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Client-side quick filter
                filterRecords(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterRecords(String query) {
        if (query.trim().isEmpty()) {
            viewModel.getAllMedicalRecords().observe(getViewLifecycleOwner(), records -> {
                if (records != null) {
                    adapter.setRecords(records);
                }
            });
        } else {
            // Simple sub-filtering matching name or symptoms
            viewModel.getAllMedicalRecords().observe(getViewLifecycleOwner(), records -> {
                if (records != null) {
                    java.util.List<MedicalRecord> filtered = new java.util.ArrayList<>();
                    for (MedicalRecord rec : records) {
                        if ((rec.getPatientName() != null && rec.getPatientName().toLowerCase().contains(query.toLowerCase())) ||
                            (rec.getDiagnosisNotes() != null && rec.getDiagnosisNotes().toLowerCase().contains(query.toLowerCase()))) {
                            filtered.add(rec);
                        }
                    }
                    adapter.setRecords(filtered);
                }
            });
        }
    }

    @Override
    public void onViewDetails(MedicalRecord record) {
        // Show complete clinical visit detail inside a polished dialog
        String details = "Patient: " + record.getPatientName() + "\n" +
                "Date: " + record.getVisitDate() + "\n" +
                "Consultant: " + record.getDoctorName() + "\n\n" +
                "--- VITALS ---\n" +
                "Height: " + record.getHeight() + " cm | Weight: " + record.getWeight() + " kg\n" +
                "Blood Pressure: " + record.getBloodPressure() + "\n" +
                "Temperature: " + record.getTemperature() + " °C | Heart Rate: " + record.getHeartRate() + " bpm\n\n" +
                "--- DIAGNOSTICS ---\n" +
                "Symptoms: " + record.getSymptoms() + "\n" +
                "Physical Exam: " + record.getPhysicalExamination() + "\n" +
                "Notes: " + record.getDiagnosisNotes() + "\n\n" +
                "--- PRESCRIPTION (Rx) ---\n" +
                record.getPrescription() + "\n\n" +
                "--- LAB RESULTS ---\n" +
                (record.getLaboratoryResult() != null ? record.getLaboratoryResult() : "None ordered");

        new AlertDialog.Builder(requireContext())
                .setTitle("Clinical Case File")
                .setMessage(details)
                .setPositiveButton("Close", null)
                .setNeutralButton("Export PDF", (dialog, which) -> onExportPdf(record))
                .show();
    }

    @Override
    public void onExportPdf(MedicalRecord record) {
        // Execute the PdfExporter utility
        File pdfFile = PdfExporter.exportMedicalRecordPdf(requireContext(), record);
        if (pdfFile != null && pdfFile.exists()) {
            Toast.makeText(getContext(), "PDF Exported Successfully!\nSaved to: " + pdfFile.getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Error exporting clinical record PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
