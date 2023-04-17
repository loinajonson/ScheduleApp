package com.example.myprojectscheduleapp;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class Signup extends AppCompatActivity {

    // Initialize variables
    EditText signupName, signupUsername, signupEmail, signupPassword;
    TextView loginRedirectText;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Assign values to the variables
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        loginRedirectText = findViewById(R.id.loginRedirectText);
        signupButton = findViewById(R.id.signup_button);

        // Sets a listener for the signup button
        signupButton.setOnClickListener(view -> {

            // Creates and instance of the firebase database
            database = FirebaseDatabase.getInstance();

            // Assigns the users from the firebase database to a variable
            reference = database.getReference("users");

            // Gets the value of the entered name, email, username and password on the signup form
            String name = signupName.getText().toString();
            String email = signupEmail.getText().toString();
            String username = signupUsername.getText().toString();
            String password = signupPassword.getText().toString();

            // Inializes a new instance of the helper class
            HelperClass helperClass = new HelperClass(name, email, username, password);

            // Assigns the value of the entered information using the helper class structure
            reference.child(username).setValue(helperClass);

            // Message below will show if the signup process is successful
            Toast.makeText(Signup.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();

            // Creates an intent to load the MainActivity.class  once successful registration is done
            Intent intent = new Intent(Signup.this, Main.class);

            // Starts the above intent
            startActivity(intent);
        });

        // Same function as the intent above but only executes when the login link has been clicked
        loginRedirectText.setOnClickListener(view -> {
            Intent intent = new Intent(Signup.this, Main.class);
            startActivity(intent);
        });
    }
}
