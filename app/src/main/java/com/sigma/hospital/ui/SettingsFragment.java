package com.sigma.hospital.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;

public class SettingsFragment extends Fragment {

    private MaterialSwitch darkModeSwitch;
    private MaterialSwitch alertsSwitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Settings");
        }

        darkModeSwitch = view.findViewById(R.id.switch_dark_mode);
        alertsSwitch = view.findViewById(R.id.switch_alerts);

        // Pre-fill theme check
        boolean isDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        darkModeSwitch.setChecked(isDark);

        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Toast.makeText(getContext(), "Dark Mode Enabled", Toast.LENGTH_SHORT).show();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Toast.makeText(getContext(), "Light Mode Enabled", Toast.LENGTH_SHORT).show();
            }
        });

        alertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String state = isChecked ? "Alert announcements enabled" : "Alert announcements silenced";
            Toast.makeText(getContext(), state, Toast.LENGTH_SHORT).show();
        });
    }
}
