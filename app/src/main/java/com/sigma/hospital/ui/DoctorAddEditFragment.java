package com.sigma.hospital.ui;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.models.Doctor;
import com.sigma.hospital.viewmodel.HospitalViewModel;

public class DoctorAddEditFragment extends Fragment {

    private HospitalViewModel viewModel;
    private Doctor editingDoctor;
    private boolean isEditMode = false;

    private TextView titleText;
    private TextInputEditText nameInput;
    private TextInputEditText specialtyInput;
    private TextInputEditText licenseInput;
    private TextInputEditText genderInput;
    private TextInputEditText phoneInput;
    private TextInputEditText emailInput;
    private TextInputEditText scheduleInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_add_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        editingDoctor = viewModel.getSelectedDoctor();
        isEditMode = (editingDoctor != null);

        titleText = view.findViewById(R.id.form_title);
        nameInput = view.findViewById(R.id.form_doc_name);
        specialtyInput = view.findViewById(R.id.form_doc_specialization);
        licenseInput = view.findViewById(R.id.form_doc_license);
        genderInput = view.findViewById(R.id.form_doc_gender);
        phoneInput = view.findViewById(R.id.form_doc_phone);
        emailInput = view.findViewById(R.id.form_doc_email);
        scheduleInput = view.findViewById(R.id.form_doc_schedule);

        MaterialButton btnSave = view.findViewById(R.id.btn_save_doctor);

        if (isEditMode) {
            titleText.setText("Edit Doctor Profile");
            prepopulateFields();
        } else {
            titleText.setText("Register Doctor Profile");
        }

        btnSave.setOnClickListener(v -> saveDoctorProfile());
    }

    private void prepopulateFields() {
        nameInput.setText(editingDoctor.getName());
        specialtyInput.setText(editingDoctor.getSpecialization());
        licenseInput.setText(editingDoctor.getLicenseNumber());
        genderInput.setText(editingDoctor.getGender());
        phoneInput.setText(editingDoctor.getPhone());
        emailInput.setText(editingDoctor.getEmail());
        scheduleInput.setText(editingDoctor.getPracticeSchedule());
    }

    private void saveDoctorProfile() {
        String name = nameInput.getText() != null ? nameInput.getText().toString().trim() : "";
        String specialty = specialtyInput.getText() != null ? specialtyInput.getText().toString().trim() : "";
        String license = licenseInput.getText() != null ? licenseInput.getText().toString().trim() : "";
        String gender = genderInput.getText() != null ? genderInput.getText().toString().trim() : "";
        String phone = phoneInput.getText() != null ? phoneInput.getText().toString().trim() : "";
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String schedule = scheduleInput.getText() != null ? scheduleInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(specialty)) {
            specialtyInput.setError("Specialization is required");
            return;
        }

        if (TextUtils.isEmpty(license)) {
            licenseInput.setError("License ID is required");
            return;
        }

        if (isEditMode) {
            editingDoctor.setName(name);
            editingDoctor.setSpecialization(specialty);
            editingDoctor.setLicenseNumber(license);
            editingDoctor.setGender(gender);
            editingDoctor.setPhone(phone);
            editingDoctor.setEmail(email);
            editingDoctor.setPracticeSchedule(schedule);

            viewModel.updateDoctor(editingDoctor);
            Toast.makeText(getContext(), "Doctor profile updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Doctor newDoctor = new Doctor();
            newDoctor.setName(name);
            newDoctor.setSpecialization(specialty);
            newDoctor.setLicenseNumber(license);
            newDoctor.setGender(gender);
            newDoctor.setPhone(phone);
            newDoctor.setEmail(email);
            newDoctor.setPracticeSchedule(schedule);
            newDoctor.setActive(true);

            viewModel.insertDoctor(newDoctor);
            Toast.makeText(getContext(), "Doctor profile registered successfully!", Toast.LENGTH_SHORT).show();
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new DoctorListFragment(), false);
        }
    }
}
