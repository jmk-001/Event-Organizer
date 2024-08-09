package com.example.assignment3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.provider.EventCategory;

import java.util.ArrayList;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.ViewHolder> {

    ArrayList<EventCategory> data = new ArrayList<>();

    public void setData(ArrayList<EventCategory> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_layout, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.categoryIdTv.setText(data.get(position).getCategoryId());
        holder.categoryNameTv.setText(data.get(position).getCategoryName());
        holder.eventCountTv.setText(String.valueOf(data.get(position).getEventCount()));
        if (data.get(position).isActive()){
            holder.isActiveTv.setText("Active");
        } else { holder.isActiveTv.setText("Inactive"); }

        holder.cardView.setOnClickListener(v -> {
            String selectedLocation = data.get(position).getEventLocation();

            Context context = holder.cardView.getContext();
            Intent intent = new Intent(context, CategoryLocationMapsActivity.class);
            intent.putExtra("location", selectedLocation);
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
        public TextView categoryIdTv;
        public TextView categoryNameTv;
        public TextView eventCountTv;
        public TextView isActiveTv;
        public View cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView;
            categoryIdTv = itemView.findViewById(R.id.tvCategoryId);
            categoryNameTv = itemView.findViewById(R.id.tvCategoryName);
            eventCountTv = itemView.findViewById(R.id.tvEventCount);
            isActiveTv = itemView.findViewById(R.id.tvCategoryIsActive);
        }
    }
}
