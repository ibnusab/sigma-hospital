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
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.ui.adapters.PatientAdapter;
import com.sigma.hospital.viewmodel.HospitalViewModel;

public class PatientListFragment extends Fragment implements PatientAdapter.OnPatientClickListener {

    private HospitalViewModel viewModel;
    private PatientAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyState;
    private TextInputEditText searchInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Patients Directory");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        recyclerView = view.findViewById(R.id.rv_patients);
        emptyState = view.findViewById(R.id.patient_empty_state);
        searchInput = view.findViewById(R.id.search_patient_input);
        FloatingActionButton fab = view.findViewById(R.id.fab_add_patient);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PatientAdapter(this);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            viewModel.setSelectedPatient(null); // Registering mode
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new PatientAddEditFragment(), true);
            }
        });

        if (!"Admin".equals(MainActivity.userRole)) {
            fab.setVisibility(View.GONE);
        }

        // Observe all patients initially
        viewModel.getAllPatients().observe(getViewLifecycleOwner(), patients -> {
            if (patients == null || patients.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                adapter.setPatients(patients);
            }
        });

        // Set up live search inputs
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPatients(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterPatients(String query) {
        if (query.trim().isEmpty()) {
            viewModel.getAllPatients().observe(getViewLifecycleOwner(), patients -> {
                if (patients != null) {
                    adapter.setPatients(patients);
                }
            });
        } else {
            viewModel.searchPatients("%" + query + "%").observe(getViewLifecycleOwner(), patients -> {
                if (patients != null) {
                    adapter.setPatients(patients);
                }
            });
        }
    }

    @Override
    public void onPatientClick(Patient patient) {
        viewModel.setSelectedPatient(patient);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new PatientDetailFragment(), true);
        }
    }
}
