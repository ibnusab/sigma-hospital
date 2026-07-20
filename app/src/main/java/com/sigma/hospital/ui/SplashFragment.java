package com.sigma.hospital.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;

public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Hide navigation layout bars while on Splash
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).hideNavigationBars();
        }

        // Post delayed handler to navigate to Login
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isAdded() && getActivity() != null) {
                ((MainActivity) getActivity()).replaceFragment(new LoginFragment(), false);
            }
        }, 2200);
    }
}
