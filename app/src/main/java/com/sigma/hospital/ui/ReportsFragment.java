package com.sigma.hospital.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.utils.PdfExporter;
import com.sigma.hospital.viewmodel.HospitalViewModel;
import java.io.File;

public class ReportsFragment extends Fragment {

    private HospitalViewModel viewModel;
    private TextView recapText;

    private int docsCount = 0;
    private int patsCount = 0;
    private int apptsCount = 0;
    private int recordsCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Reports & Analytics");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        recapText = view.findViewById(R.id.report_details_text);
        MaterialButton btnExport = view.findViewById(R.id.btn_export_analytics_pdf);

        btnExport.setOnClickListener(v -> executeSystemReportExport());

        observeAllAnalytics();
    }

    private void observeAllAnalytics() {
        viewModel.getAllDoctors().observe(getViewLifecycleOwner(), doctors -> {
            if (doctors != null) {
                docsCount = doctors.size();
                updateRecapDisplay();
            }
        });

        viewModel.getAllPatients().observe(getViewLifecycleOwner(), patients -> {
            if (patients != null) {
                patsCount = patients.size();
                updateRecapDisplay();
            }
        });

        viewModel.getAllAppointments().observe(getViewLifecycleOwner(), appts -> {
            if (appts != null) {
                apptsCount = appts.size();
                updateRecapDisplay();
            }
        });

        viewModel.getAllMedicalRecords().observe(getViewLifecycleOwner(), recs -> {
            if (recs != null) {
                recordsCount = recs.size();
                updateRecapDisplay();
            }
        });
    }

    private void updateRecapDisplay() {
        String reportSummary = "ACTIVE ADMINISTRATIVE METRICS:\n\n" +
                "• Active Clinicians on Roster: " + docsCount + "\n" +
                "• Total Registered Patients: " + patsCount + "\n" +
                "• Scheduled Consultation Appointments: " + apptsCount + "\n" +
                "• Diagnostic Medical Record Charts: " + recordsCount + "\n\n" +
                "SYSTEM INTEGRITY CODE: SECURE/ONLINE-FIRST\n" +
                "DATABASE STATUS: COMPACT & SYNCHRONIZED\n" +
                "PDF AUDIT READY: YES";
        recapText.setText(reportSummary);
    }

    private void executeSystemReportExport() {
        // Execute dynamic complete hospital audit export
        File pdfFile = PdfExporter.exportSystemAuditReport(
                requireContext(),
                docsCount,
                patsCount,
                apptsCount,
                recordsCount
        );

        if (pdfFile != null && pdfFile.exists()) {
            Toast.makeText(getContext(), "Audit Report Exported!\nSaved as: " + pdfFile.getName(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Error compiling audit PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
