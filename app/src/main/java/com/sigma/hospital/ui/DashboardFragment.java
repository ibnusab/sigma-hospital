package com.sigma.hospital.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.button.MaterialButton;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.viewmodel.HospitalViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private HospitalViewModel viewModel;
    private TextView totalDoctorsText;
    private TextView totalPatientsText;
    private TextView appointmentsTodayText;
    private TextView totalRecordsText;
    private TextView welcomeTitleText;
    private TextView dateText;
    private BarChart barChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).showNavigationBars();
            ((MainActivity) getActivity()).updateActionBarTitle("Sigma Dashboard");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        totalDoctorsText = view.findViewById(R.id.stat_total_doctors);
        totalPatientsText = view.findViewById(R.id.stat_total_patients);
        appointmentsTodayText = view.findViewById(R.id.stat_appointments_today);
        totalRecordsText = view.findViewById(R.id.stat_total_records);
        welcomeTitleText = view.findViewById(R.id.dash_welcome_title);
        dateText = view.findViewById(R.id.dash_date_now);
        barChart = view.findViewById(R.id.dashboard_bar_chart);

        // Customize welcome title based on session role
        String welcomeGreeting;
        if ("Doctor".equals(MainActivity.userRole)) {
            welcomeGreeting = "Welcome Back, Dr. John Doe";
        } else if ("Patient".equals(MainActivity.userRole)) {
            welcomeGreeting = "Welcome Back, Jane Doe";
        } else {
            welcomeGreeting = "Good Morning, Administrator";
        }
        welcomeTitleText.setText(welcomeGreeting);

        // Set date string
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault());
        dateText.setText("Date: " + sdf.format(new Date()));

        // Bind role-specific dashboard views
        View cardDoctors = view.findViewById(R.id.card_stat_doctors);
        View cardPatients = view.findViewById(R.id.card_stat_patients);
        View cardAppointments = view.findViewById(R.id.card_stat_appointments);
        View cardRecords = view.findViewById(R.id.card_stat_records);

        TextView lblDoctors = view.findViewById(R.id.lbl_stat_doctors);
        TextView lblAppointments = view.findViewById(R.id.lbl_stat_appointments);

        // Bind quick actions
        MaterialButton quickDoctor = view.findViewById(R.id.btn_quick_doctor);
        MaterialButton quickPatient = view.findViewById(R.id.btn_quick_patient);
        MaterialButton quickAppointment = view.findViewById(R.id.btn_quick_appointment);

        if ("Admin".equals(MainActivity.userRole)) {
            if (cardDoctors != null) cardDoctors.setVisibility(View.VISIBLE);
            if (cardPatients != null) cardPatients.setVisibility(View.VISIBLE);
            if (cardAppointments != null) cardAppointments.setVisibility(View.VISIBLE);
            if (cardRecords != null) cardRecords.setVisibility(View.VISIBLE);

            if (quickDoctor != null) {
                quickDoctor.setVisibility(View.VISIBLE);
                quickDoctor.setText("Add Doc");
                quickDoctor.setIconResource(android.R.drawable.ic_menu_add);
                quickDoctor.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new DoctorAddEditFragment(), true);
                    }
                });
            }
            if (quickPatient != null) {
                quickPatient.setVisibility(View.VISIBLE);
                quickPatient.setText("Add Pat");
                quickPatient.setIconResource(android.R.drawable.ic_menu_myplaces);
                quickPatient.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new PatientAddEditFragment(), true);
                    }
                });
            }
            if (quickAppointment != null) {
                quickAppointment.setVisibility(View.VISIBLE);
                quickAppointment.setText("Book Appt");
                quickAppointment.setIconResource(android.R.drawable.ic_menu_today);
                quickAppointment.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new AppointmentFragment(), true);
                    }
                });
            }
        } else if ("Doctor".equals(MainActivity.userRole)) {
            if (cardDoctors != null) cardDoctors.setVisibility(View.GONE);
            if (cardPatients != null) cardPatients.setVisibility(View.VISIBLE);
            if (cardAppointments != null) cardAppointments.setVisibility(View.VISIBLE);
            if (cardRecords != null) cardRecords.setVisibility(View.VISIBLE);

            if (quickDoctor != null) {
                quickDoctor.setVisibility(View.VISIBLE);
                quickDoctor.setText("Schedule");
                quickDoctor.setIconResource(android.R.drawable.ic_menu_my_calendar);
                quickDoctor.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new ScheduleFragment(), true);
                    }
                });
            }
            if (quickPatient != null) {
                quickPatient.setVisibility(View.VISIBLE);
                quickPatient.setText("Patients");
                quickPatient.setIconResource(android.R.drawable.ic_menu_myplaces);
                quickPatient.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new PatientListFragment(), true);
                    }
                });
            }
            if (quickAppointment != null) {
                quickAppointment.setVisibility(View.VISIBLE);
                quickAppointment.setText("Records");
                quickAppointment.setIconResource(android.R.drawable.ic_menu_gallery);
                quickAppointment.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new MedicalRecordFragment(), true);
                    }
                });
            }
        } else if ("Patient".equals(MainActivity.userRole)) {
            if (cardDoctors != null) cardDoctors.setVisibility(View.VISIBLE);
            if (cardPatients != null) cardPatients.setVisibility(View.GONE);
            if (cardAppointments != null) cardAppointments.setVisibility(View.VISIBLE);
            if (cardRecords != null) cardRecords.setVisibility(View.GONE);

            if (lblDoctors != null) lblDoctors.setText("Ready Doctors");
            if (lblAppointments != null) lblAppointments.setText("My Bookings");

            if (quickDoctor != null) {
                quickDoctor.setVisibility(View.VISIBLE);
                quickDoctor.setText("Find Doctor");
                quickDoctor.setIconResource(android.R.drawable.ic_menu_search);
                quickDoctor.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new DoctorListFragment(), true);
                    }
                });
            }
            if (quickPatient != null) {
                quickPatient.setVisibility(View.GONE);
            }
            if (quickAppointment != null) {
                quickAppointment.setVisibility(View.VISIBLE);
                quickAppointment.setText("Book Now");
                quickAppointment.setIconResource(android.R.drawable.ic_menu_today);
                quickAppointment.setOnClickListener(v -> {
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(new AppointmentFragment(), true);
                    }
                });
            }
        }

        observeStatistics();
        setupChart();
    }

    private void observeStatistics() {
        viewModel.getAllDoctors().observe(getViewLifecycleOwner(), doctors -> {
            if (doctors != null) {
                totalDoctorsText.setText(String.valueOf(doctors.size()));
            }
        });

        viewModel.getAllPatients().observe(getViewLifecycleOwner(), patients -> {
            if (patients != null) {
                totalPatientsText.setText(String.valueOf(patients.size()));
            }
        });

        viewModel.getAllAppointments().observe(getViewLifecycleOwner(), appts -> {
            if (appts != null) {
                appointmentsTodayText.setText(String.valueOf(appts.size()));
            }
        });

        viewModel.getAllMedicalRecords().observe(getViewLifecycleOwner(), recs -> {
            if (recs != null) {
                totalRecordsText.setText(String.valueOf(recs.size()));
            }
        });
    }

    private void setupChart() {
        if (barChart == null) return;

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f, 15f));
        entries.add(new BarEntry(2f, 22f));
        entries.add(new BarEntry(3f, 30f));
        entries.add(new BarEntry(4f, 18f));
        entries.add(new BarEntry(5f, 25f));
        entries.add(new BarEntry(6f, 35f));

        BarDataSet dataSet = new BarDataSet(entries, "Appointments Filled");
        dataSet.setColor(Color.parseColor("#2C5E7A")); // Calming medical slate-blue
        dataSet.setValueTextColor(Color.parseColor("#2C3E50")); // Soft dark slate text
        dataSet.setValueTextSize(10f);

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(true);
        barChart.animateY(1000);
        barChart.invalidate();
    }

    private float spToPx(float sp) {
        return sp * getResources().getDisplayMetrics().scaledDensity;
    }
}
