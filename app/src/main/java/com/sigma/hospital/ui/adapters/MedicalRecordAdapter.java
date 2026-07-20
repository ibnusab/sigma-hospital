package com.sigma.hospital.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sigma.hospital.R;
import com.sigma.hospital.models.MedicalRecord;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.MedicalRecordViewHolder> {

    private List<MedicalRecord> records = new ArrayList<>();
    private OnRecordActionListener actionListener;

    public interface OnRecordActionListener {
        void onViewDetails(MedicalRecord record);
        void onExportPdf(MedicalRecord record);
    }

    public MedicalRecordAdapter(OnRecordActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void setRecords(List<MedicalRecord> records) {
        this.records = records != null ? records : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicalRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medical_record, parent, false);
        return new MedicalRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordViewHolder holder, int position) {
        MedicalRecord record = records.get(position);
        holder.bind(record, actionListener);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class MedicalRecordViewHolder extends RecyclerView.ViewHolder {
        private final TextView patientName;
        private final TextView dateTextView;
        private final TextView doctorName;
        private final TextView symptomsTextView;
        private final TextView diagnosisTextView;
        private final Button btnExportPdf;
        private final Button btnView;

        public MedicalRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.rec_patient_name);
            dateTextView = itemView.findViewById(R.id.rec_date);
            doctorName = itemView.findViewById(R.id.rec_doctor_name);
            symptomsTextView = itemView.findViewById(R.id.rec_symptoms);
            diagnosisTextView = itemView.findViewById(R.id.rec_diagnosis);
            btnExportPdf = itemView.findViewById(R.id.btn_export_pdf);
            btnView = itemView.findViewById(R.id.btn_view_details);
        }

        public void bind(MedicalRecord record, OnRecordActionListener listener) {
            patientName.setText(record.getPatientName() != null ? record.getPatientName() : "Patient File");
            dateTextView.setText(record.getVisitDate() != null ? record.getVisitDate() : "Date N/A");
            doctorName.setText("Diagnosed by: " + (record.getDoctorName() != null ? record.getDoctorName() : "Consultant Physician"));
            
            String symptoms = record.getSymptoms();
            symptomsTextView.setText("Symptoms: " + (symptoms != null && !symptoms.isEmpty() ? symptoms : "General assessment"));

            String diagnosis = record.getDiagnosisNotes();
            diagnosisTextView.setText("Diagnosis: " + (diagnosis != null && !diagnosis.isEmpty() ? diagnosis : "Routine clinical clearance"));

            if (listener != null) {
                btnView.setOnClickListener(v -> listener.onViewDetails(record));
                btnExportPdf.setOnClickListener(v -> listener.onExportPdf(record));
            }
        }
    }
}
