package com.sigma.hospital.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sigma.hospital.R;
import com.sigma.hospital.models.NotificationAlert;
import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationAlert> alerts = new ArrayList<>();
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(NotificationAlert alert);
    }

    public NotificationAdapter(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    public void setAlerts(List<NotificationAlert> alerts) {
        this.alerts = alerts != null ? alerts : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationAlert alert = alerts.get(position);
        holder.bind(alert, listener);
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView messageTextView;
        private final TextView timeTextView;
        private final ImageView iconView;
        private final View unreadIndicator;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.notif_title);
            messageTextView = itemView.findViewById(R.id.notif_message);
            timeTextView = itemView.findViewById(R.id.notif_time);
            iconView = itemView.findViewById(R.id.notif_icon);
            unreadIndicator = itemView.findViewById(R.id.notif_unread_indicator);
        }

        public void bind(NotificationAlert alert, OnNotificationClickListener listener) {
            titleTextView.setText(alert.getTitle());
            messageTextView.setText(alert.getMessage());
            timeTextView.setText(alert.getTimestamp());

            if (alert.isRead()) {
                unreadIndicator.setVisibility(View.GONE);
            } else {
                unreadIndicator.setVisibility(View.VISIBLE);
            }

            // Map icons based on types
            String type = alert.getType() != null ? alert.getType().toLowerCase() : "general";
            if ("appointment".equals(type)) {
                iconView.setImageResource(android.R.drawable.ic_menu_today);
            } else if ("alert".equals(type)) {
                iconView.setImageResource(android.R.drawable.ic_dialog_alert);
            } else {
                iconView.setImageResource(android.R.drawable.ic_popup_reminder);
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNotificationClick(alert);
                }
            });
        }
    }
}
