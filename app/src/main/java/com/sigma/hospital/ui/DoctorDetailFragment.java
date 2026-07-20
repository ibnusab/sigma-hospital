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
import com.sigma.hospital.models.Doctor;
import com.sigma.hospital.utils.BarcodeGenerator;
import com.sigma.hospital.viewmodel.HospitalViewModel;

public class DoctorDetailFragment extends Fragment {

    private HospitalViewModel viewModel;
    private Doctor doctor;

    private TextView nameText;
    private TextView specialtyText;
    private TextView licenseText;
    private TextView genderText;
    private TextView phoneText;
    private TextView emailText;
    private TextView scheduleText;
    private TextView statusBadge;
    private ImageView qrImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        doctor = viewModel.getSelectedDoctor();

        if (doctor == null) {
            Toast.makeText(getContext(), "Error: Doctor record not found", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new DoctorListFragment(), false);
            }
            return;
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Doctor Profile");
        }

        nameText = view.findViewById(R.id.detail_doc_name);
        specialtyText = view.findViewById(R.id.detail_doc_specialty);
        licenseText = view.findViewById(R.id.detail_doc_license);
        genderText = view.findViewById(R.id.detail_doc_gender);
        phoneText = view.findViewById(R.id.detail_doc_phone);
        emailText = view.findViewById(R.id.detail_doc_email);
        scheduleText = view.findViewById(R.id.detail_doc_schedule);
        statusBadge = view.findViewById(R.id.detail_doc_status_badge);
        qrImageView = view.findViewById(R.id.detail_doc_qr);

        MaterialButton btnEdit = view.findViewById(R.id.btn_edit_doctor);
        MaterialButton btnDelete = view.findViewById(R.id.btn_delete_doctor);
        View layoutAdminActions = view.findViewById(R.id.layout_admin_doctor_actions);
        MaterialButton btnBookDoctor = view.findViewById(R.id.btn_book_doctor);

        if ("Admin".equals(MainActivity.userRole)) {
            if (layoutAdminActions != null) layoutAdminActions.setVisibility(View.VISIBLE);
            if (btnBookDoctor != null) btnBookDoctor.setVisibility(View.GONE);
        } else if ("Patient".equals(MainActivity.userRole)) {
            if (layoutAdminActions != null) layoutAdminActions.setVisibility(View.GONE);
            if (btnBookDoctor != null) {
                btnBookDoctor.setVisibility(View.VISIBLE);
                btnBookDoctor.setOnClickListener(v -> {
                    AppointmentFragment apptFrag = new AppointmentFragment();
                    Bundle args = new Bundle();
                    args.putString("prefilled_doctor_name", doctor.getName());
                    apptFrag.setArguments(args);
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).replaceFragment(apptFrag, true);
                    }
                });
            }
        } else {
            if (layoutAdminActions != null) layoutAdminActions.setVisibility(View.GONE);
            if (btnBookDoctor != null) btnBookDoctor.setVisibility(View.GONE);
        }

        bindDoctorData();

        if (btnEdit != null) {
            btnEdit.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).replaceFragment(new DoctorAddEditFragment(), true);
                }
            });
        }

        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                viewModel.deleteDoctor(doctor);
                Toast.makeText(getContext(), "Doctor record deleted successfully", Toast.LENGTH_SHORT).show();
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).replaceFragment(new DoctorListFragment(), false);
                }
            });
        }
    }

    private void bindDoctorData() {
        nameText.setText(doctor.getName());
        specialtyText.setText(doctor.getSpecialization());
        licenseText.setText(doctor.getLicenseNumber() != null ? doctor.getLicenseNumber() : "No active license ID");
        genderText.setText(doctor.getGender() != null ? doctor.getGender() : "Not specified");
        phoneText.setText(doctor.getPhone() != null ? doctor.getPhone() : "No phone listed");
        emailText.setText(doctor.getEmail() != null ? doctor.getEmail() : "No email listed");
        scheduleText.setText(doctor.getPracticeSchedule() != null ? doctor.getPracticeSchedule() : "Practice Schedule N/A");

        if (doctor.isActive()) {
            statusBadge.setText("ACTIVE");
            statusBadge.setBackgroundResource(R.drawable.badge_success);
        } else {
            statusBadge.setText("INACTIVE");
            statusBadge.setBackgroundResource(R.drawable.badge_warning);
        }

        // Generate QR verification bitmap dynamically
        try {
            String verificationData = "SIGMA-DOCTOR: ID=" + doctor.getId() + "\nNAME=" + doctor.getName() + "\nSPECIALTY=" + doctor.getSpecialization();
            Bitmap qrBitmap = BarcodeGenerator.generateQRCode(verificationData, 400);
            if (qrBitmap != null) {
                qrImageView.setImageBitmap(qrBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
