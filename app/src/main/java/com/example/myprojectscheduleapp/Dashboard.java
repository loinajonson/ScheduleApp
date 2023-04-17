package com.example.myprojectscheduleapp;

import static com.example.myprojectscheduleapp.R.id;
import static com.example.myprojectscheduleapp.R.layout;
import static com.example.myprojectscheduleapp.R.string;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.material.navigation.NavigationView;



public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_dashboard);

        Toolbar toolbar = findViewById(id.toolbar);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);}
        catch (NullPointerException e){ e.printStackTrace();}

        drawerLayout = findViewById(id.drawer_layout);
        NavigationView navigationView = findViewById(id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, string.open_nav,
                string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();



    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == id.nav_New_Meeting) {
            getSupportFragmentManager().beginTransaction().replace(id.fragment_container, new New_Meeting()).commit();
        } else if (item.getItemId() == id.nav_logout){
            super.onBackPressed();
            finish();
        }

        drawerLayout.closeDrawers();
        return true;
    }



    @Override
    public void onBackPressed() {
        if (getDrawerToggleDelegate().isNavigationVisible())  {
            drawerLayout.closeDrawers();
        }

    }
}
