package com.sigma.hospital.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sigma.hospital.R;
import com.sigma.hospital.models.Appointment;
import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments = new ArrayList<>();
    private OnPaymentClickListener paymentClickListener;

    public interface OnPaymentClickListener {
        void onPaymentClick(Appointment appointment);
    }

    public AppointmentAdapter() {
    }

    public AppointmentAdapter(OnPaymentClickListener paymentClickListener) {
        this.paymentClickListener = paymentClickListener;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments != null ? appointments : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.bind(appointment, paymentClickListener);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView patientNameTextView;
        private final TextView doctorNameTextView;
        private final TextView dateTimeTextView;
        private final TextView statusBadge;
        private final TextView notesTextView;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameTextView = itemView.findViewById(R.id.app_patient_name);
            doctorNameTextView = itemView.findViewById(R.id.app_doctor_name);
            dateTimeTextView = itemView.findViewById(R.id.app_date_time);
            statusBadge = itemView.findViewById(R.id.app_status_badge);
            notesTextView = itemView.findViewById(R.id.app_notes);
        }

        public void bind(Appointment appointment, OnPaymentClickListener listener) {
            patientNameTextView.setText(appointment.getPatientName() != null ? appointment.getPatientName() : "Patient Record");
            doctorNameTextView.setText("Consultant: " + (appointment.getDoctorName() != null ? appointment.getDoctorName() : "Assigned Physician"));
            dateTimeTextView.setText(appointment.getDate() + " at " + appointment.getTime());
            
            String notes = appointment.getNotes();
            notesTextView.setText("Reason: " + (notes != null && !notes.isEmpty() ? notes : "General Consultation"));

            String status = appointment.getStatus() != null ? appointment.getStatus().toUpperCase() : "SCHEDULED";
            statusBadge.setText(status);

            switch (status) {
                case "COMPLETED":
                    statusBadge.setBackgroundResource(R.drawable.badge_success);
                    break;
                case "CANCELLED":
                    statusBadge.setBackgroundResource(R.drawable.badge_danger);
                    break;
                default:
                    statusBadge.setBackgroundResource(R.drawable.badge_warning);
                    break;
            }

            // Set up Payment Views
            View paymentLayout = itemView.findViewById(R.id.layout_appointment_payment);
            TextView paymentBadge = itemView.findViewById(R.id.app_payment_badge);
            View btnPay = itemView.findViewById(R.id.btn_pay_appointment);

            if (paymentBadge != null) {
                String payStatus = appointment.getPaymentStatus();
                paymentBadge.setText(payStatus.toUpperCase());
                if ("PAID".equals(payStatus)) {
                    paymentBadge.setBackgroundResource(R.drawable.badge_success);
                    if (btnPay != null) btnPay.setVisibility(View.GONE);
                } else {
                    paymentBadge.setBackgroundResource(R.drawable.badge_warning);
                    if (btnPay != null) {
                        btnPay.setVisibility(View.VISIBLE);
                        btnPay.setOnClickListener(v -> {
                            if (listener != null) {
                                listener.onPaymentClick(appointment);
                            }
                        });
                    }
                }
            }
        }
    }
}
