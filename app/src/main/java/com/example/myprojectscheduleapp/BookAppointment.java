package com.example.myprojectscheduleapp;


import static java.lang.String.valueOf;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class BookAppointment extends AppCompatActivity implements BottomSheetListener {
    private String strSelectedDate;
    private String selectedTimeSlot;
    
    
    private EditText tvemail;
    private TextView tvSelectedDate, tvSelectedTime;
    private DatePickerDialog datePickerDialog;
    private Button btnSchedule;

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        // Declaring variables
        tvemail = findViewById(R.id.tvemail);
        
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);
        btnSchedule = findViewById(R.id.btnSchedule);
        ImageView imgDate = findViewById(R.id.imgDate);
        ImageView imgTime = findViewById(R.id.imgTime);
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // Setting the toolbar
        Toolbar toolBar = findViewById(R.id.toolBar);
        toolBar.setTitle(R.string.str_title_schedule);
        setSupportActionBar(toolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }




        // Creates an instance of the datepicker
        datePickerDialog = new DatePickerDialog(this,
                (datePicker, year1, monthOfYear, dayOfMonth) ->
                        onDateSelection(year1, monthOfYear, dayOfMonth),
                year, month, day);

        // Onclick listener for the datepicker
        imgDate.setOnClickListener(view -> datePickerDialog.show());
        imgTime.setOnClickListener(view ->

                // uses the time slot bottom sheet to display the timeslots available
                new TimeSlotBottomSheet().show(getSupportFragmentManager(),
                        AppConstants.TAG_BS_TIME_SLOT));

        // Listener for the schedule button, calls the btnScheduleClick function
        btnSchedule.setOnClickListener(view -> btnScheduleClick());



    }

    // Set the selected date to a predefined format
    private void onDateSelection(int year1, int monthOfYear, int dayOfMonth) {
        strSelectedDate = AppGlobal.getFormattedDate(
                AppGlobal.getDateFromString(
                        dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1)
        );
        tvSelectedDate.setText(strSelectedDate);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(BookAppointment.this, Dashboard.class));
        return true;
    }


    // Ensures that the selected time matches one of the options on the predefined
    // time slots available
    @Override
    public void onBottomSheetAction(Object object) {
        if (object instanceof SheetActionPojo) {
            SheetActionPojo sheetActionPojo = (SheetActionPojo) object;
            if (sheetActionPojo.getActionType().equals(AppConstants.TAG_BS_TIME_SLOT)) {
                onTimeSlotSelected(sheetActionPojo.getParam1());
            }
        }
    }

    // When the timeslot is selected the value is converted to text and assigned to
    // the variable tvSelectedTime
    private void onTimeSlotSelected(String selectedTimeSlot) {
        this.selectedTimeSlot = selectedTimeSlot;
        tvSelectedTime.setText(selectedTimeSlot);
    }


    // When the user clicks the schedule button AppGlobal will verify the validity of the
    // inputted date. If this date is valid and selectedTimeSlot is valid then it will
    // show the confirm dialog.
    private void btnScheduleClick() {
        if (AppGlobal.isStringValid(strSelectedDate)) {
            if (AppGlobal.isStringValid(selectedTimeSlot)) {

                // Creates and instance of the firebase database
                firebaseDatabase = FirebaseDatabase.getInstance();

                databaseReference = firebaseDatabase.getReference("appointments");

                String appointmentemail = tvemail.getText().toString();
                String appointmentTime = tvSelectedTime.getText().toString();
                String appointmentDate = tvSelectedDate.getText().toString();

                AppointmentHelperClass appointmentHelperClass = new AppointmentHelperClass( appointmentemail, appointmentTime, appointmentDate);

                databaseReference.child(appointmentDate).push().setValue(appointmentHelperClass);

                showScheduleConfirmDialog();
            } else {

                // shows when the time slot is empty
                AppGlobal.showSnackBar(btnSchedule, getString(R.string.str_validation_time_slot_empty));
            }
        } else {

            // shows when the date is empty

            AppGlobal.showSnackBar(btnSchedule, getString(R.string.str_validation_date_empty));
        }
    }


    // Dialog pop-up when schedule is confirmed
    private boolean showScheduleConfirmDialog() {


            resetForm();


      new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.popup_title_success_schedule)
                .setMessage(String.format(valueOf(R.string.popup_msg_success_schedule), strSelectedDate, selectedTimeSlot))
                .setPositiveButton(R.string.str_okay, (dialogInterface, i) -> {
                    finish();
                })
                .show();
        return true;

    }

    // resets the form
    void resetForm() {
        tvSelectedDate.setText("");
        tvSelectedTime.setText("");
        tvemail.setText("");
    }


    }

