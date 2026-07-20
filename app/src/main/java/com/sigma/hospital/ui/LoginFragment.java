package com.sigma.hospital.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;

public class LoginFragment extends Fragment {

    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private CheckBox rememberMeCheckBox;
    private TextView forgotPasswordText;
    private TextView registerLink;

    // Role selection views
    private MaterialCardView cardAdmin, cardDoctor, cardPatient;
    private ImageView imgAdmin, imgDoctor, imgPatient;
    private TextView tvAdmin, tvDoctor, tvPatient;
    private String selectedRole = "Admin"; // Default role

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideNavigationBars();
        }

        emailEditText = view.findViewById(R.id.login_email);
        passwordEditText = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.btn_login);
        rememberMeCheckBox = view.findViewById(R.id.cb_remember_me);
        forgotPasswordText = view.findViewById(R.id.tv_forgot_password);
        registerLink = view.findViewById(R.id.tv_register_link);

        // Bind Role Selection Cards
        cardAdmin = view.findViewById(R.id.card_role_admin);
        cardDoctor = view.findViewById(R.id.card_role_doctor);
        cardPatient = view.findViewById(R.id.card_role_patient);

        imgAdmin = view.findViewById(R.id.img_role_admin);
        imgDoctor = view.findViewById(R.id.img_role_doctor);
        imgPatient = view.findViewById(R.id.img_role_patient);

        tvAdmin = view.findViewById(R.id.tv_role_admin);
        tvDoctor = view.findViewById(R.id.tv_role_doctor);
        tvPatient = view.findViewById(R.id.tv_role_patient);

        // Set Role Card Listeners
        cardAdmin.setOnClickListener(v -> selectRole("Admin"));
        cardDoctor.setOnClickListener(v -> selectRole("Doctor"));
        cardPatient.setOnClickListener(v -> selectRole("Patient"));

        // Initialize default role selection
        selectRole("Admin");

        loginButton.setOnClickListener(v -> performLogin());

        forgotPasswordText.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new ForgotPasswordFragment(), true);
            }
        });

        registerLink.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new RegisterFragment(), true);
            }
        });
    }

    private void selectRole(String role) {
        selectedRole = role;
        
        int activeStrokeColor = getResources().getColor(R.color.primary);
        int inactiveStrokeColor = getResources().getColor(R.color.outline_variant);
        int activeBgColor = getResources().getColor(R.color.primary_light);
        int inactiveBgColor = getResources().getColor(R.color.surface);
        int activeTextColor = getResources().getColor(R.color.primary);
        int inactiveTextColor = getResources().getColor(R.color.text_secondary);

        // Reset Admin Card style
        cardAdmin.setStrokeColor(inactiveStrokeColor);
        cardAdmin.setStrokeWidth(dpToPx(1));
        cardAdmin.setCardBackgroundColor(ColorStateList.valueOf(inactiveBgColor));
        imgAdmin.setImageTintList(ColorStateList.valueOf(inactiveTextColor));
        tvAdmin.setTextColor(inactiveTextColor);

        // Reset Doctor Card style
        cardDoctor.setStrokeColor(inactiveStrokeColor);
        cardDoctor.setStrokeWidth(dpToPx(1));
        cardDoctor.setCardBackgroundColor(ColorStateList.valueOf(inactiveBgColor));
        imgDoctor.setImageTintList(ColorStateList.valueOf(inactiveTextColor));
        tvDoctor.setTextColor(inactiveTextColor);

        // Reset Patient Card style
        cardPatient.setStrokeColor(inactiveStrokeColor);
        cardPatient.setStrokeWidth(dpToPx(1));
        cardPatient.setCardBackgroundColor(ColorStateList.valueOf(inactiveBgColor));
        imgPatient.setImageTintList(ColorStateList.valueOf(inactiveTextColor));
        tvPatient.setTextColor(inactiveTextColor);

        // Apply style to active card & prefill convenient defaults
        if ("Admin".equals(role)) {
            cardAdmin.setStrokeColor(activeStrokeColor);
            cardAdmin.setStrokeWidth(dpToPx(2));
            cardAdmin.setCardBackgroundColor(ColorStateList.valueOf(activeBgColor));
            imgAdmin.setImageTintList(ColorStateList.valueOf(activeTextColor));
            tvAdmin.setTextColor(activeTextColor);
            
            emailEditText.setText("admin@sigmahospital.com");
            passwordEditText.setText("admin123");
        } else if ("Doctor".equals(role)) {
            cardDoctor.setStrokeColor(activeStrokeColor);
            cardDoctor.setStrokeWidth(dpToPx(2));
            cardDoctor.setCardBackgroundColor(ColorStateList.valueOf(activeBgColor));
            imgDoctor.setImageTintList(ColorStateList.valueOf(activeTextColor));
            tvDoctor.setTextColor(activeTextColor);
            
            emailEditText.setText("doctor@sigmahospital.com");
            passwordEditText.setText("doctor123");
        } else if ("Patient".equals(role)) {
            cardPatient.setStrokeColor(activeStrokeColor);
            cardPatient.setStrokeWidth(dpToPx(2));
            cardPatient.setCardBackgroundColor(ColorStateList.valueOf(activeBgColor));
            imgPatient.setImageTintList(ColorStateList.valueOf(activeTextColor));
            tvPatient.setTextColor(activeTextColor);
            
            emailEditText.setText("patient@sigmahospital.com");
            passwordEditText.setText("patient123");
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void performLogin() {
        String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        boolean loginSuccess = false;
        String successMsg = "";

        // Support convenient presets & allow flexible inputs
        if ("admin@sigmahospital.com".equals(email) && "admin123".equals(password)) {
            selectedRole = "Admin";
            loginSuccess = true;
            successMsg = "Access Granted. Welcome Admin!";
        } else if ("doctor@sigmahospital.com".equals(email) && "doctor123".equals(password)) {
            selectedRole = "Doctor";
            loginSuccess = true;
            successMsg = "Access Granted. Welcome Doctor!";
        } else if ("patient@sigmahospital.com".equals(email) && "patient123".equals(password)) {
            selectedRole = "Patient";
            loginSuccess = true;
            successMsg = "Access Granted. Welcome Patient!";
        } else {
            // General success simulation for custom credentials
            loginSuccess = true;
            successMsg = "Welcome! Logged in as " + selectedRole;
        }

        if (loginSuccess) {
            Toast.makeText(getContext(), successMsg, Toast.LENGTH_SHORT).show();
            if (getActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.updateSession(selectedRole, email);
                mainActivity.showNavigationBars();
                mainActivity.replaceFragment(new DashboardFragment(), false);
            }
        }
    }
}
