package com.example.myprojectscheduleapp;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotBottomSheet extends BottomSheetDialogFragment {

    private BottomSheetListener bottomSheetListener;
    private RecyclerView recyclerView;
    private TimeSlotAdapter timeSlotAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bottomSheetListener = (BottomSheetListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_time_slot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        prepareCurrencyList();
    }

    private void prepareCurrencyList() {
        timeSlotAdapter = new TimeSlotAdapter(getTimeSlotList());
        recyclerView.setAdapter(timeSlotAdapter);
        setItemClickListener();
    }

    private void setItemClickListener() {
        timeSlotAdapter.setOnItemClickListener(object
                -> onCurrencyItemClick((String) object));
    }

    private void onCurrencyItemClick(String currency) {
        dismiss();
        if (getTag() != null) {
            SheetActionPojo sheetActionPojo = new SheetActionPojo();
            sheetActionPojo.setActionType(getTag());
            sheetActionPojo.setParam1(currency);
            bottomSheetListener.onBottomSheetAction(sheetActionPojo);
        }
    }

    private List<String> getTimeSlotList() {
        ArrayList<String> timeSlotArrayList = new ArrayList<>();
        timeSlotArrayList.add("10 am to 11 am");
        timeSlotArrayList.add("11 am to 12 pm");
        timeSlotArrayList.add("12 pm to 1 pm");
        timeSlotArrayList.add("1 pm to 2 pm");
        timeSlotArrayList.add("2 pm to 3 pm");
        timeSlotArrayList.add("3 pm to 4 pm");
        timeSlotArrayList.add("4 pm to 5 pm");
        timeSlotArrayList.add("5 pm to 6 pm");
        return timeSlotArrayList;
    }

}
