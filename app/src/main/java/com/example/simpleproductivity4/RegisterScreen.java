package com.example.simpleproductivity4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class RegisterScreen extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private TextView back_to_login, signup_button;
    private EditText signup_username_edittext, signup_email_edittext, signup_password_edittext;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
        mAuth = FirebaseAuth.getInstance();
        back_to_login = findViewById(R.id.back_to_login);
        back_to_login.setOnClickListener(this);
        signup_username_edittext = findViewById(R.id.signup_username_edittext);
        signup_email_edittext = findViewById(R.id.signup_email_edittext);
        signup_password_edittext = findViewById(R.id.signup_password_edittext);
        signup_button = findViewById(R.id.sign_up_button);
        signup_button.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_login:
                startActivity(new Intent(this, LoginScreen.class));
                finish();
                break;
            case R.id.sign_up_button:
                createAccount();
                break;
        }
    }

    private void createAccount() {
        String username = signup_email_edittext.getText().toString().trim();
        String email = signup_email_edittext.getText().toString().trim();
        String password = signup_password_edittext.getText().toString().trim();

        if (email.isEmpty()) {
            signup_email_edittext.setError("Email is required");
            signup_email_edittext.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signup_email_edittext.setError("Please provide a valid email address");
            signup_email_edittext.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            signup_password_edittext.setError("Password is required");
            signup_password_edittext.requestFocus();
            return;
        }
        if (password.length() < 6) {
            signup_password_edittext.setError("Min password length should be 6 characters!");
            signup_password_edittext.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            signup_username_edittext.setError("Confirming Password is required");
            signup_username_edittext.requestFocus();
            return;
        }
        if (username.length() < 6) {
            signup_username_edittext.setError("Min password length should be 6 characters!");
            signup_username_edittext.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Signup", "createUserWithEmailSuccess");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterScreen.this, "Account created, please verify your email",
                                    Toast.LENGTH_LONG).show();
                            sendEmailVerification();
                        } else {
                            Log.w("Signup", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterScreen.this, "Sign Up failed",
                                    Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.GONE);

                    }
                });

    }

    private void goToDashboard() {
        startActivity(new Intent(this, Dashboard.class));
    }

    private void updateUI(FirebaseUser user) {

    }

    public void sendEmailVerification() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user1 = auth.getCurrentUser();

        user1.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("signup", "Email sent.");
                        }
                    }
                });
    }


}