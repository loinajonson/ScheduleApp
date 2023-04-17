package com.example.myprojectscheduleapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.Objects;

public class Main extends AppCompatActivity {
    // Initialize variables
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    EditText loginUsername,loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Assign variable for google sign in
        btSignIn = findViewById(R.id.bt_sign_in);

        // Initialize sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("569585886502-ok8gr9jbp4bqut0l2gtdruh4guj7lfi4.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(Main.this, googleSignInOptions);

        btSignIn.setOnClickListener((View.OnClickListener) view -> {
            // Initialize sign in intent
            Intent intent = googleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, 100);
        });

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in redirect to profile activity
            startActivity(new Intent(Main.this, BookAppointment.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        // Assign variable for firebase database
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signupRedirectText = findViewById(R.id.signupRedirectText);

        // Set up listener for the login button
        // When login button is clicked, validates that the username and passwords are not empty
        // the checkUser function will be called
        loginButton.setOnClickListener(view -> {
            if (!validateUsername() | !validatePassword()) {

            } else {
                checkUser();
            }
        });

        // Set up listener for the sign up link
        signupRedirectText.setOnClickListener(view -> {
            // Redirects the page to the signup page once the sign up link is clicked
            Intent intent = new Intent(Main.this, Signup.class);
            startActivity(intent);
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, task -> {
                            // Check condition
                            if (task.isSuccessful()) {
                                // When task is successful redirect to profile activity display Toast
                                startActivity(new Intent(Main.this, Dashboard.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                displayToast("Firebase authentication successful");
                            } else {
                                // When task is unsuccessful display Toast
                                displayToast("Authentication Failed :" + Objects.requireNonNull(task.getException()).getMessage());
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    // Checks if the username is empty
    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {

            // if username is empty the message below will be shown
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {

            // If username is not empty no error will be shown
            loginUsername.setError(null);
            return true;
        }
    }

    // Checks if the password is empty
    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {

            // If password is empty the message below will be shown
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {

            // if password is not empty no error message will be shown
            loginPassword.setError(null);
            return true;
        }
    }

    // Gets called when the username and password are not empty
    public void checkUser() {

        // Assigning the value of the inputted username and password to a variable
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();

        // Creates an instance of the firebase database
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        // Validates if the username entered is a match to any username in the firebase database
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            //  If data is read data from the Database, you receive the data as a DataSnapshot.
            // This function handles the said DataSnapshot received.
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // happens when a datasnapshot has been received
                if (snapshot.exists()) {

                    // No error will be shown
                    loginUsername.setError(null);

                    // traversing into the snapshot by calling child() to return child snapshots
                    // Getting the value of password that is stored on firebase database based on the username entered
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                    // Asserting that the password is not null, since one of the parameters of creating a user account is
                    // setting a password this should always be not null
                    // Added to avoid null exception
                    assert passwordFromDB != null;

                    // When the password for the username entered matches the password in the database this happens
                    if (passwordFromDB.equals(userPassword)) {

                        // No error will be displayed
                        loginUsername.setError(null);

                        // Gets the value of the name, email and username from the firebase database
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);

                        // Creates new intent from this to Dashboard.class
                        Intent intent = new Intent(Main.this, Dashboard.class);

                        // the following puts the extended data to the intent.
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        startActivity(intent);
                    } else {

                        // When the password entered by the user does not match the firebase database
                        // The following error would then be displayed
                        loginPassword.setError("Invalid Credentials");

                        // focuses on the password field
                        loginPassword.requestFocus();
                    }
                } else {

                    // When the username entered by the user does not match any username in the firebase database
                    // The following error would then be displayed
                    loginUsername.setError("User does not exist");

                    // focuses on the username field
                    loginUsername.requestFocus();
                }
            }

            // This happens when the user cancels the process
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



    }
}
