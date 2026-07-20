package com.sigma.hospital.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sigma.hospital.MainActivity;
import com.sigma.hospital.R;
import com.sigma.hospital.models.NotificationAlert;
import com.sigma.hospital.ui.adapters.NotificationAdapter;
import com.sigma.hospital.viewmodel.HospitalViewModel;

public class NotificationsFragment extends Fragment implements NotificationAdapter.OnNotificationClickListener {

    private HospitalViewModel viewModel;
    private NotificationAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayout emptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBarTitle("Notifications");
        }

        viewModel = new ViewModelProvider(requireActivity()).get(HospitalViewModel.class);

        recyclerView = view.findViewById(R.id.rv_notifications);
        emptyState = view.findViewById(R.id.notif_empty_state);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(this);
        recyclerView.setAdapter(adapter);

        // Mark all as read when opening notifications
        viewModel.markAllNotificationsAsRead();

        viewModel.getAllNotifications().observe(getViewLifecycleOwner(), alerts -> {
            if (alerts == null || alerts.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyState.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyState.setVisibility(View.GONE);
                adapter.setAlerts(alerts);
            }
        });
    }

    @Override
    public void onNotificationClick(NotificationAlert alert) {
        // Display toast alert details
        Toast.makeText(getContext(), alert.getTitle() + ":\n" + alert.getMessage(), Toast.LENGTH_LONG).show();
    }
}
