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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;

public class RegisterFragment extends Fragment {

    private TextInputEditText nameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton registerButton;
    private TextView loginLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideNavigationBars();
        }

        nameEditText = view.findViewById(R.id.register_name);
        emailEditText = view.findViewById(R.id.register_email);
        passwordEditText = view.findViewById(R.id.register_password);
        registerButton = view.findViewById(R.id.btn_register);
        loginLink = view.findViewById(R.id.tv_login_link);

        registerButton.setOnClickListener(v -> performRegistration());

        loginLink.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new LoginFragment(), false);
            }
        });
    }

    private void performRegistration() {
        String name = nameEditText.getText() != null ? nameEditText.getText().toString().trim() : "";
        String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
        String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Full name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email address is required");
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        Toast.makeText(getContext(), "Administrator account created successfully! Please Sign In.", Toast.LENGTH_LONG).show();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new LoginFragment(), false);
        }
    }
}
