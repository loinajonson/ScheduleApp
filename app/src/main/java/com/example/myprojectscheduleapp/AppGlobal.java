package com.example.myprojectscheduleapp;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class AppGlobal {


    public static void showToast(Context context, String message) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }


    public static void showSnackBar(View view, String message) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    public static Date getDateFromString(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        try {
            return simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public static boolean isStringValid(String value) {
        return null != value && value.trim().length() > 0;
    }
    public static String getFormattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        return simpleDateFormat.format(date);
    }
}