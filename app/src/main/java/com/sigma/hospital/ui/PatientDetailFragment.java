package com.sigma.hospital.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.utils.BarcodeGenerator;
import com.sigma.hospital.viewmodel.HospitalViewModel;

public class PatientDetailFragment extends Fragment {

    private HospitalViewModel viewModel;
    private Patient patient;

    private TextView nameText;
    private TextView mrnText;
    private TextView nidText;
    private TextView genderDobText;
    private TextView bloodText;
    private TextView contactText;
    private TextView addressText;
    private TextView emergencyText;
    private TextView insuranceText;
    private TextView historyText;
    private ImageView barcodeImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        patient = viewModel.getSelectedPatient();

        if (patient == null) {
            Toast.makeText(getContext(), "Error: Patient record not found", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new PatientListFragment(), false);
            }
            return;
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Patient File");
        }

        nameText = view.findViewById(R.id.detail_patient_name);
        mrnText = view.findViewById(R.id.detail_patient_mrn);
        nidText = view.findViewById(R.id.detail_patient_nid);
        genderDobText = view.findViewById(R.id.detail_patient_gender_dob);
        bloodText = view.findViewById(R.id.detail_patient_blood);
        contactText = view.findViewById(R.id.detail_patient_contact);
        addressText = view.findViewById(R.id.detail_patient_address);
        emergencyText = view.findViewById(R.id.detail_patient_emergency);
        insuranceText = view.findViewById(R.id.detail_patient_insurance);
        historyText = view.findViewById(R.id.detail_patient_history);
        barcodeImageView = view.findViewById(R.id.detail_patient_barcode);

        MaterialButton btnEdit = view.findViewById(R.id.btn_edit_patient);
        MaterialButton btnDelete = view.findViewById(R.id.btn_delete_patient);
        MaterialButton btnCreateRecord = view.findViewById(R.id.btn_create_medical_record);

        // Apply Role-Based Access controls
        if ("Admin".equals(MainActivity.userRole)) {
            if (btnEdit != null) btnEdit.setVisibility(View.VISIBLE);
            if (btnDelete != null) btnDelete.setVisibility(View.VISIBLE);
            if (btnCreateRecord != null) btnCreateRecord.setVisibility(View.VISIBLE);
        } else if ("Doctor".equals(MainActivity.userRole)) {
            if (btnEdit != null) btnEdit.setVisibility(View.VISIBLE);
            if (btnDelete != null) btnDelete.setVisibility(View.GONE);
            if (btnCreateRecord != null) btnCreateRecord.setVisibility(View.VISIBLE);
        } else { // Patient
            if (btnEdit != null) btnEdit.setVisibility(View.GONE);
            if (btnDelete != null) btnDelete.setVisibility(View.GONE);
            if (btnCreateRecord != null) btnCreateRecord.setVisibility(View.GONE);
        }

        bindPatientData();

        btnEdit.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new PatientAddEditFragment(), true);
            }
        });

        btnDelete.setOnClickListener(v -> {
            viewModel.deletePatient(patient);
            Toast.makeText(getContext(), "Patient record removed from directory", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new PatientListFragment(), false);
            }
        });

        btnCreateRecord.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new MedicalRecordAddEditFragment(), true);
            }
        });
    }

    private void bindPatientData() {
        nameText.setText(patient.getName());
        mrnText.setText("MRN: " + patient.getMedicalRecordNumber());
        nidText.setText(patient.getNationalId() != null ? patient.getNationalId() : "N/A");
        
        String gender = patient.getGender() != null ? patient.getGender() : "Not specified";
        String dob = patient.getDateOfBirth() != null ? patient.getDateOfBirth() : "DOB Unknown";
        genderDobText.setText(gender + " / " + dob);

        bloodText.setText(patient.getBloodType() != null ? "Blood Group: " + patient.getBloodType() : "Blood Group: Unknown");
        
        String phone = patient.getPhone() != null ? patient.getPhone() : "No phone";
        String email = patient.getEmail() != null ? patient.getEmail() : "No email";
        contactText.setText(phone + " | " + email);

        addressText.setText(patient.getAddress() != null ? patient.getAddress() : "No active address listed");
        emergencyText.setText(patient.getEmergencyContact() != null ? patient.getEmergencyContact() : "None registered");
        insuranceText.setText(patient.getInsurance() != null ? patient.getInsurance() : "No active insurance listed");
        historyText.setText(patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "No chronic history recorded");

        // Generate dynamic scannable CODE_128 clinical barcode
        try {
            Bitmap barcodeBitmap = BarcodeGenerator.generateBarcode(patient.getMedicalRecordNumber(), 520, 128);
            if (barcodeBitmap != null) {
                barcodeImageView.setImageBitmap(barcodeBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
