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
import com.sigma.hospital.models.MedicalRecord;
import com.sigma.hospital.models.Patient;
import com.sigma.hospital.viewmodel.HospitalViewModel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MedicalRecordAddEditFragment extends Fragment {

    private HospitalViewModel viewModel;
    private Patient activePatient;

    private TextView patientTextHeader;
    private TextView doctorTextHeader;

    private TextInputEditText heightInput;
    private TextInputEditText weightInput;
    private TextInputEditText bpInput;
    private TextInputEditText tempInput;
    private TextInputEditText hrInput;
    private TextInputEditText rrInput;
    private TextInputEditText symptomsInput;
    private TextInputEditText examInput;
    private TextInputEditText notesInput;
    private TextInputEditText prescriptionInput;
    private TextInputEditText labsInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_medical_record_add_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);
        activePatient = viewModel.getSelectedPatient();

        if (activePatient == null) {
            Toast.makeText(getContext(), "Error: Please select a patient first", Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new PatientListFragment(), false);
            }
            return;
        }

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Record Clinical Visit");
        }

        patientTextHeader = view.findViewById(R.id.rec_form_patient_name);
        doctorTextHeader = view.findViewById(R.id.rec_form_doctor_name);

        heightInput = view.findViewById(R.id.mr_form_height);
        weightInput = view.findViewById(R.id.mr_form_weight);
        bpInput = view.findViewById(R.id.mr_form_bp);
        tempInput = view.findViewById(R.id.mr_form_temp);
        hrInput = view.findViewById(R.id.mr_form_hr);
        rrInput = view.findViewById(R.id.mr_form_rr);
        symptomsInput = view.findViewById(R.id.mr_form_symptoms);
        examInput = view.findViewById(R.id.mr_form_physical_exam);
        notesInput = view.findViewById(R.id.mr_form_diagnosis_notes);
        prescriptionInput = view.findViewById(R.id.mr_form_prescription);
        labsInput = view.findViewById(R.id.mr_form_lab_results);

        MaterialButton btnSave = view.findViewById(R.id.btn_save_medical_record);

        // Bind Read-Only metadata headers
        patientTextHeader.setText("Patient File: " + activePatient.getName() + " (" + activePatient.getMedicalRecordNumber() + ")");
        
        // Fetch selected doctor name or default to general duty consultant
        String docName = "Dr. Sarah Jenkins";
        if (viewModel.getSelectedDoctor() != null) {
            docName = viewModel.getSelectedDoctor().getName();
        }
        doctorTextHeader.setText("Consulting Physician: " + docName);

        // Default Vitals pre-fills for speed
        heightInput.setText("175");
        weightInput.setText("70");
        bpInput.setText("120/80");
        tempInput.setText("36.6");
        hrInput.setText("72");
        rrInput.setText("16");

        final String activeDocName = docName;
        btnSave.setOnClickListener(v -> saveMedicalRecord(activeDocName));
    }

    private void saveMedicalRecord(String consultantName) {
        String heightStr = heightInput.getText() != null ? heightInput.getText().toString().trim() : "";
        String weightStr = weightInput.getText() != null ? weightInput.getText().toString().trim() : "";
        String bpStr = bpInput.getText() != null ? bpInput.getText().toString().trim() : "";
        String tempStr = tempInput.getText() != null ? tempInput.getText().toString().trim() : "";
        String hrStr = hrInput.getText() != null ? hrInput.getText().toString().trim() : "";
        String rrStr = rrInput.getText() != null ? rrInput.getText().toString().trim() : "";
        String symptoms = symptomsInput.getText() != null ? symptomsInput.getText().toString().trim() : "";
        String exam = examInput.getText() != null ? examInput.getText().toString().trim() : "";
        String notes = notesInput.getText() != null ? notesInput.getText().toString().trim() : "";
        String rx = prescriptionInput.getText() != null ? prescriptionInput.getText().toString().trim() : "";
        String labs = labsInput.getText() != null ? labsInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(notes)) {
            notesInput.setError("Diagnosis notes are required");
            return;
        }

        MedicalRecord record = new MedicalRecord();
        record.setPatientId(activePatient.getId());
        record.setPatientName(activePatient.getName());
        record.setPatientMrn(activePatient.getMedicalRecordNumber());
        record.setDoctorName(consultantName);
        record.setVisitDate(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        
        record.setHeight(heightStr);
        record.setWeight(weightStr);
        record.setBloodPressure(bpStr);
        record.setTemperature(tempStr);
        record.setHeartRate(hrStr);
        record.setRespiratoryRate(rrStr);
        
        record.setSymptoms(symptoms);
        record.setPhysicalExamination(exam);
        record.setDiagnosisNotes(notes);
        record.setPrescription(rx);
        record.setLaboratoryResult(labs);

        viewModel.insertMedicalRecord(record);
        Toast.makeText(getContext(), "Patient Medical Case Record Registered Successfully!", Toast.LENGTH_SHORT).show();

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new MedicalRecordFragment(), false);
        }
    }
}
