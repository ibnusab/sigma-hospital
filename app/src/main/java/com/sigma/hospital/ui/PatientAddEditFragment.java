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
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.viewmodel.HospitalViewModel;
import java.util.Random;

public class PatientAddEditFragment extends Fragment {

    private HospitalViewModel viewModel;
    private Patient editingPatient;
    private boolean isEditMode = false;

    private TextView titleText;
    private TextInputEditText mrnInput;
    private TextInputEditText nidInput;
    private TextInputEditText nameInput;
    private TextInputEditText genderInput;
    private TextInputEditText dobInput;
    private TextInputEditText bloodInput;
    private TextInputEditText phoneInput;
    private TextInputEditText emailInput;
    private TextInputEditText addressInput;
    private TextInputEditText emergencyInput;
    private TextInputEditText insuranceInput;
    private TextInputEditText historyInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_add_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        editingPatient = viewModel.getSelectedPatient();
        isEditMode = (editingPatient != null);

        titleText = view.findViewById(R.id.patient_form_title);
        mrnInput = view.findViewById(R.id.form_patient_mrn);
        nidInput = view.findViewById(R.id.form_patient_nid);
        nameInput = view.findViewById(R.id.form_patient_name);
        genderInput = view.findViewById(R.id.form_patient_gender);
        dobInput = view.findViewById(R.id.form_patient_dob);
        bloodInput = view.findViewById(R.id.form_patient_blood);
        phoneInput = view.findViewById(R.id.form_patient_phone);
        emailInput = view.findViewById(R.id.form_patient_email);
        addressInput = view.findViewById(R.id.form_patient_address);
        emergencyInput = view.findViewById(R.id.form_patient_emergency);
        insuranceInput = view.findViewById(R.id.form_patient_insurance);
        historyInput = view.findViewById(R.id.form_patient_history);

        MaterialButton btnSave = view.findViewById(R.id.btn_save_patient);

        if (isEditMode) {
            titleText.setText("Edit Patient File");
            prepopulateFields();
        } else {
            titleText.setText("Register New Patient");
            // Auto generate standard format clinical MRN
            Random rand = new Random();
            int num = 1000 + rand.nextInt(9000);
            mrnInput.setText("MRN-2026-" + num);
        }

        btnSave.setOnClickListener(v -> savePatientProfile());
    }

    private void prepopulateFields() {
        mrnInput.setText(editingPatient.getMedicalRecordNumber());
        nidInput.setText(editingPatient.getNationalId());
        nameInput.setText(editingPatient.getName());
        genderInput.setText(editingPatient.getGender());
        dobInput.setText(editingPatient.getDateOfBirth());
        bloodInput.setText(editingPatient.getBloodType());
        phoneInput.setText(editingPatient.getPhone());
        emailInput.setText(editingPatient.getEmail());
        addressInput.setText(editingPatient.getAddress());
        emergencyInput.setText(editingPatient.getEmergencyContact());
        insuranceInput.setText(editingPatient.getInsurance());
        historyInput.setText(editingPatient.getMedicalHistory());
    }

    private void savePatientProfile() {
        String mrn = mrnInput.getText() != null ? mrnInput.getText().toString().trim() : "";
        String nid = nidInput.getText() != null ? nidInput.getText().toString().trim() : "";
        String name = nameInput.getText() != null ? nameInput.getText().toString().trim() : "";
        String gender = genderInput.getText() != null ? genderInput.getText().toString().trim() : "";
        String dob = dobInput.getText() != null ? dobInput.getText().toString().trim() : "";
        String blood = bloodInput.getText() != null ? bloodInput.getText().toString().trim() : "";
        String phone = phoneInput.getText() != null ? phoneInput.getText().toString().trim() : "";
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String address = addressInput.getText() != null ? addressInput.getText().toString().trim() : "";
        String emergency = emergencyInput.getText() != null ? emergencyInput.getText().toString().trim() : "";
        String insurance = insuranceInput.getText() != null ? insuranceInput.getText().toString().trim() : "";
        String history = historyInput.getText() != null ? historyInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(mrn)) {
            mrnInput.setError("Medical Record Number is required");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Patient full name is required");
            return;
        }

        if (isEditMode) {
            editingPatient.setMedicalRecordNumber(mrn);
            editingPatient.setNationalId(nid);
            editingPatient.setName(name);
            editingPatient.setGender(gender);
            editingPatient.setDateOfBirth(dob);
            editingPatient.setBloodType(blood);
            editingPatient.setPhone(phone);
            editingPatient.setEmail(email);
            editingPatient.setAddress(address);
            editingPatient.setEmergencyContact(emergency);
            editingPatient.setInsurance(insurance);
            editingPatient.setMedicalHistory(history);

            viewModel.updatePatient(editingPatient);
            Toast.makeText(getContext(), "Patient details updated successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Patient newPatient = new Patient();
            newPatient.setMedicalRecordNumber(mrn);
            newPatient.setNationalId(nid);
            newPatient.setName(name);
            newPatient.setGender(gender);
            newPatient.setDateOfBirth(dob);
            newPatient.setBloodType(blood);
            newPatient.setPhone(phone);
            newPatient.setEmail(email);
            newPatient.setAddress(address);
            newPatient.setEmergencyContact(emergency);
            newPatient.setInsurance(insurance);
            newPatient.setMedicalHistory(history);

            viewModel.insertPatient(newPatient);
            Toast.makeText(getContext(), "Patient directory file registered successfully!", Toast.LENGTH_SHORT).show();
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new PatientListFragment(), false);
        }
    }
}
