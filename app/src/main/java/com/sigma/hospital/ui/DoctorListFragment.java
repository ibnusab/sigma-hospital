package com.sigma.hospital.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.models.Doctor;
import com.sigma.hospital.ui.adapters.DoctorAdapter;
import com.sigma.hospital.viewmodel.HospitalViewModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorListFragment extends Fragment implements DoctorAdapter.OnDoctorClickListener {

    private HospitalViewModel viewModel;
    private DoctorAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyState;
    private TextInputEditText searchInput;
    
    private String currentSearchQuery = "";
    private boolean showReadyOnly = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Doctors Directory");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        recyclerView = view.findViewById(R.id.rv_doctors);
        emptyState = view.findViewById(R.id.doctor_empty_state);
        searchInput = view.findViewById(R.id.search_doctor_input);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_doctor);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DoctorAdapter(this);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            viewModel.setSelectedDoctor(null); // Registering mode
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new DoctorAddEditFragment(), true);
            }
        });

        if (!"Admin".equals(MainActivity.userRole)) {
            fab.setVisibility(View.GONE);
        }

        // Bind MaterialSwitch filter
        com.google.android.material.materialswitch.MaterialSwitch switchReady = view.findViewById(R.id.switch_ready_only);
        if (switchReady != null) {
            switchReady.setOnCheckedChangeListener((buttonView, isChecked) -> {
                showReadyOnly = isChecked;
                applyFilters();
            });
        }

        // Observe all doctors initially and perform filtering
        applyFilters();

        // Set up searching
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void applyFilters() {
        if (currentSearchQuery.trim().isEmpty()) {
            viewModel.getAllDoctors().observe(getViewLifecycleOwner(), doctors -> {
                if (doctors != null) {
                    List<Doctor> filtered = new ArrayList<>();
                    for (Doctor d : doctors) {
                        if (!showReadyOnly || d.isActive()) {
                            filtered.add(d);
                        }
                    }
                    updateDoctorsListUI(filtered);
                }
            });
        } else {
            viewModel.searchDoctors(currentSearchQuery).observe(getViewLifecycleOwner(), doctors -> {
                if (doctors != null) {
                    List<Doctor> filtered = new ArrayList<>();
                    for (Doctor d : doctors) {
                        if (!showReadyOnly || d.isActive()) {
                            filtered.add(d);
                        }
                    }
                    updateDoctorsListUI(filtered);
                }
            });
        }
    }

    private void updateDoctorsListUI(List<Doctor> doctors) {
        if (doctors == null || doctors.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            adapter.setDoctors(doctors);
        }
    }

    @Override
    public void onDoctorClick(Doctor doctor) {
        viewModel.setSelectedDoctor(doctor);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new DoctorDetailFragment(), true);
        }
    }
}
