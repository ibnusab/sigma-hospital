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

public class ForgotPasswordFragment extends Fragment {

    private TextInputEditText emailEditText;
    private MaterialButton sendResetButton;
    private TextView backToLoginText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideNavigationBars();
        }

        emailEditText = view.findViewById(R.id.reset_email);
        sendResetButton = view.findViewById(R.id.btn_send_reset);
        backToLoginText = view.findViewById(R.id.tv_back_to_login);

        sendResetButton.setOnClickListener(v -> sendResetLink());

        backToLoginText.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).replaceFragment(new LoginFragment(), false);
            }
        });
    }

    private void sendResetLink() {
        String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email address is required");
            return;
        }

        Toast.makeText(getContext(), "Recovery link sent to: " + email, Toast.LENGTH_LONG).show();
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new LoginFragment(), false);
        }
    }
}
