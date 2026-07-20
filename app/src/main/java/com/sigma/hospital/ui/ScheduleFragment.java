package com.sigma.hospital.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.models.DoctorSchedule;
import com.sigma.hospital.viewmodel.HospitalViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private HospitalViewModel viewModel;
    private LinearLayout listContainer;

    private TextInputEditText doctorInput;
    private TextInputEditText dateInput;
    private TextInputEditText startInput;
    private TextInputEditText endInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Shift Roster");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        listContainer = view.findViewById(R.id.schedules_list_container);
        doctorInput = view.findViewById(R.id.shift_doctor_input);
        dateInput = view.findViewById(R.id.shift_date_input);
        startInput = view.findViewById(R.id.shift_start_input);
        endInput = view.findViewById(R.id.shift_end_input);
        MaterialButton btnSave = view.findViewById(R.id.btn_save_shift);

        // Bind role-specific containers
        View cardRegisterShift = view.findViewById(R.id.card_register_shift);
        View cardDoctorAbsen = view.findViewById(R.id.card_doctor_absen);

        if ("Admin".equals(MainActivity.userRole)) {
            if (cardRegisterShift != null) cardRegisterShift.setVisibility(View.VISIBLE);
            if (cardDoctorAbsen != null) cardDoctorAbsen.setVisibility(View.GONE);
        } else if ("Doctor".equals(MainActivity.userRole)) {
            if (cardRegisterShift != null) cardRegisterShift.setVisibility(View.GONE);
            if (cardDoctorAbsen != null) {
                cardDoctorAbsen.setVisibility(View.VISIBLE);
                
                TextView tvAbsenStatus = view.findViewById(R.id.tv_absen_status);
                MaterialButton btnClockIn = view.findViewById(R.id.btn_clock_in);
                MaterialButton btnClockOut = view.findViewById(R.id.btn_clock_out);
                
                if (btnClockIn != null) {
                    btnClockIn.setOnClickListener(v -> {
                        if (tvAbsenStatus != null) {
                            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            tvAbsenStatus.setText("Current Status: CLOCKED IN at " + time);
                            tvAbsenStatus.setTextColor(getResources().getColor(R.color.secondary));
                        }
                        Toast.makeText(getContext(), "Duty Clock-In recorded successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
                
                if (btnClockOut != null) {
                    btnClockOut.setOnClickListener(v -> {
                        if (tvAbsenStatus != null) {
                            String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                            tvAbsenStatus.setText("Current Status: CLOCKED OUT at " + time);
                            tvAbsenStatus.setTextColor(getResources().getColor(R.color.danger));
                        }
                        Toast.makeText(getContext(), "Duty Clock-Out recorded successfully!", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        } else {
            if (cardRegisterShift != null) cardRegisterShift.setVisibility(View.GONE);
            if (cardDoctorAbsen != null) cardDoctorAbsen.setVisibility(View.GONE);
        }

        // Pre-fill defaults
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        dateInput.setText(currentDate);
        startInput.setText("08:00");
        endInput.setText("16:00");

        btnSave.setOnClickListener(v -> saveShiftSchedule());

        viewModel.getAllSchedules().observe(getViewLifecycleOwner(), schedules -> {
            if (schedules != null) {
                renderSchedulesList(schedules);
            }
        });
    }

    private void saveShiftSchedule() {
        String docName = doctorInput.getText() != null ? doctorInput.getText().toString().trim() : "";
        String dateStr = dateInput.getText() != null ? dateInput.getText().toString().trim() : "";
        String startStr = startInput.getText() != null ? startInput.getText().toString().trim() : "";
        String endStr = endInput.getText() != null ? endInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(docName)) {
            doctorInput.setError("Doctor name is required");
            return;
        }

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(1); // Set a default ID
        schedule.setDoctorName(docName);
        schedule.setDate(dateStr);
        schedule.setStartTime(startStr);
        schedule.setEndTime(endStr);
        schedule.setAvailable(true);

        viewModel.insertSchedule(schedule);
        Toast.makeText(getContext(), "Weekly shift roster saved!", Toast.LENGTH_SHORT).show();

        doctorInput.setText("");
    }

    private void renderSchedulesList(java.util.List<DoctorSchedule> schedules) {
        listContainer.removeAllViews();
        if (schedules.isEmpty()) {
            TextView emptyText = new TextView(getContext());
            emptyText.setText("No registered rosters listed. Create a shift above.");
            emptyText.setTextSize(14);
            emptyText.setPadding(16, 16, 16, 16);
            listContainer.addView(emptyText);
            return;
        }

        for (DoctorSchedule sched : schedules) {
            View card = LayoutInflater.from(getContext()).inflate(R.layout.item_doctor, listContainer, false);
            TextView docName = card.findViewById(R.id.doc_name);
            TextView specialty = card.findViewById(R.id.doc_specialty);
            TextView scheduleTime = card.findViewById(R.id.doc_schedule);
            TextView badge = card.findViewById(R.id.doc_status_badge);

            docName.setText(sched.getDoctorName() != null ? sched.getDoctorName() : "Doctor Shift");
            specialty.setText("Duty Date: " + sched.getDate());
            scheduleTime.setText("Work Shift hours: " + sched.getStartTime() + " - " + sched.getEndTime());
            badge.setText(sched.isAvailable() ? "AVAILABLE" : "ON DUTY");
            badge.setBackgroundResource(sched.isAvailable() ? R.drawable.badge_success : R.drawable.badge_warning);

            listContainer.addView(card);
        }
    }
}
