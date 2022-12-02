package edu.bluejack22_1.kofi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack22_1.kofi.R;
import edu.bluejack22_1.kofi.interfaces.RecyclerViewInterface;
import edu.bluejack22_1.kofi.controller.model.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private ArrayList<Notification> notifications;
    private RecyclerViewInterface recyclerViewInterface;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.notifications = notifications;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notification,parent,false);
        return new NotificationViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.nameView.setText(notification.getUser().getFullName());
        holder.contentView.setText(notification.getContent());
        holder.timeView.setText(notification.getDateCreated().toDate().toString());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private TextView nameView, contentView, timeView;
        private ImageView delete;
        public NotificationViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            nameView = itemView.findViewById(R.id.card_notification_name);
            contentView = itemView.findViewById(R.id.card_notification_text);
            timeView = itemView.findViewById(R.id.card_notification_time);
            delete = itemView.findViewById(R.id.card_notification_delete);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Log.d("NOTIFICATIONS", position + "");
                        recyclerViewInterface.onClickDelete(position);
                    }
                }
            });
        }
    }
}
