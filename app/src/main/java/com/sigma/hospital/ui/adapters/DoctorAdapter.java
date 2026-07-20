package com.sigma.hospital.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sigma.hospital.R;
import com.sigma.hospital.models.Doctor;
import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctors = new ArrayList<>();
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onDoctorClick(Doctor doctor);
    }

    public DoctorAdapter(OnDoctorClickListener listener) {
        this.listener = listener;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors != null ? doctors : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = doctors.get(position);
        holder.bind(doctor, listener);
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView specialtyTextView;
        private final TextView scheduleTextView;
        private final TextView statusBadge;
        private final ImageView avatarImageView;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.doc_name);
            specialtyTextView = itemView.findViewById(R.id.doc_specialty);
            scheduleTextView = itemView.findViewById(R.id.doc_schedule);
            statusBadge = itemView.findViewById(R.id.doc_status_badge);
            avatarImageView = itemView.findViewById(R.id.doc_avatar);
        }

        public void bind(Doctor doctor, OnDoctorClickListener listener) {
            nameTextView.setText(doctor.getName());
            specialtyTextView.setText(doctor.getSpecialization());
            scheduleTextView.setText(doctor.getPracticeSchedule() != null ? doctor.getPracticeSchedule() : "No active schedule");
            
            if (doctor.isActive()) {
                statusBadge.setText("ACTIVE");
                statusBadge.setBackgroundResource(R.drawable.badge_success);
            } else {
                statusBadge.setText("INACTIVE");
                statusBadge.setBackgroundResource(R.drawable.badge_warning);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDoctorClick(doctor);
                }
            });
        }
    }
}
