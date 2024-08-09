package com.example.assignment3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.provider.Event;

import java.util.ArrayList;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    ArrayList<Event> data = new ArrayList<Event>();

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    public ArrayList<Event> getData() {
        return this.data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.eventIdTv.setText(data.get(position).getEventId());
        holder.categoryIdTv.setText(data.get(position).getCategoryId());
        holder.eventNameTv.setText(data.get(position).getEventName());
        holder.ticketsAvailableTv.setText(String.valueOf(data.get(position).getTicketsAvailable()));
        if (data.get(position).isActive()){
            holder.isActiveTv.setText("Active");
        } else { holder.isActiveTv.setText("Inactive"); }

        holder.cardView.setOnClickListener(v -> {
            String eventNameString = data.get(position).getEventName();

            Context context = holder.cardView.getContext();
            Intent intent = new Intent(context, EventGoogleActivity.class);
            intent.putExtra("eventName", eventNameString);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (this.data != null){
            return this.data.size();
        } return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventIdTv;
        public TextView categoryIdTv;
        public TextView eventNameTv;
        public TextView ticketsAvailableTv;
        public TextView isActiveTv;
        public View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            eventIdTv = itemView.findViewById(R.id.tvCategoryId);
            categoryIdTv = itemView.findViewById(R.id.tvCategoryName);
            eventNameTv = itemView.findViewById(R.id.tvEventCount);
            ticketsAvailableTv = itemView.findViewById(R.id.tvCategoryIsActive);
            isActiveTv = itemView.findViewById(R.id.tvEventIsActive);
        }
    }
}
