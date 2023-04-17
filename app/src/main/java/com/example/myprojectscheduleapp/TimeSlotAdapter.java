package com.example.myprojectscheduleapp;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myprojectscheduleapp.R;
import com.example.myprojectscheduleapp.RecyclerViewClickListener;

import java.util.List;

public final class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ItemHolder> {

    private final List<String> timeSlotList;
    public RecyclerViewClickListener recyclerViewClickListener;

    public TimeSlotAdapter(List<String> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    public void setOnItemClickListener(RecyclerViewClickListener recyclerViewClickListener) {
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_time_slot, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        populateItemView(holder, position);
    }

    private void populateItemView(ItemHolder holder, int position) {
        if (position >= 0) {
            holder.tvTitle.setText(timeSlotList.get(position));
            holder.itemView.setTag(timeSlotList.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return timeSlotList.size();
    }


    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public View itemView;

        public ItemHolder(@NonNull android.view.View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            this.itemView.setOnClickListener(view -> recyclerViewClickListener.onItemClick(view.getTag()));
        }
    }

}