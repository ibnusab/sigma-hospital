package com.sigma.hospital.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sigma.hospital.R;
import com.sigma.hospital.models.Patient;
import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.PatientViewHolder> {

    private List<Patient> patients = new ArrayList<>();
    private OnPatientClickListener listener;

    public interface OnPatientClickListener {
        void onPatientClick(Patient patient);
    }

    public PatientAdapter(OnPatientClickListener listener) {
        this.listener = listener;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients != null ? patients : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        holder.bind(patient, listener);
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView mrnTextView;
        private final TextView phoneTextView;
        private final TextView bloodBadge;
        private final ImageView avatarImageView;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.patient_name);
            mrnTextView = itemView.findViewById(R.id.patient_mrn);
            phoneTextView = itemView.findViewById(R.id.patient_phone);
            bloodBadge = itemView.findViewById(R.id.patient_blood_badge);
            avatarImageView = itemView.findViewById(R.id.patient_avatar);
        }

        public void bind(Patient patient, OnPatientClickListener listener) {
            nameTextView.setText(patient.getName());
            mrnTextView.setText("MRN: " + patient.getMedicalRecordNumber());
            phoneTextView.setText(patient.getPhone() != null && !patient.getPhone().isEmpty() ? patient.getPhone() : "No phone number");
            bloodBadge.setText(patient.getBloodType() != null && !patient.getBloodType().isEmpty() ? patient.getBloodType() : "N/A");

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onPatientClick(patient);
                }
            });
        }
    }
}
