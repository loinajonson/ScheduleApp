package com.example.myprojectscheduleapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class New_Meeting extends Fragment {

    public Activity activity;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

        Intent intent = new Intent(getActivity(), BookAppointment.class);
        startActivity(intent);
    }
}