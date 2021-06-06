package com.example.simpleproductivity4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private TextView signUp, login_button, forgot_password;
    private EditText login_email_edittext, login_password_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();
        signUp = findViewById(R.id.sign_up_text);
        signUp.setOnClickListener(this);

        login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(this);

        forgot_password = findViewById(R.id.forgot_password_text);
        forgot_password.setOnClickListener(this);


        login_email_edittext = findViewById(R.id.login_email_edittext);
        login_password_edittext = findViewById(R.id.login_password_edittext);

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
            case R.id.sign_up_text:
                startActivity(new Intent(this, RegisterScreen.class));
                break;
            case R.id.login_button:
                login();
                break;
            case R.id.forgot_password_text:
                startActivity(new Intent(this, PasswordReset.class));
                break;
        }
    }

    private void login() {
        String email = login_email_edittext.getText().toString().trim();
        String password = login_password_edittext.getText().toString().trim();

        if (email.isEmpty()) {
            login_email_edittext.setError("Email is required");
            login_email_edittext.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            login_password_edittext.setError("Password is required");
            login_password_edittext.requestFocus();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("login", "loginWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginScreen.this, "Log in successful",
                                    Toast.LENGTH_LONG).show();
                            goToDashboard();
                        } else {
                            Log.w("login", "loginWithEmail:failure", task.getException());
                            Toast.makeText(LoginScreen.this, "Log in Failed",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }


    private void goToDashboard() {
        startActivity(new Intent(this, Dashboard.class));
    }

}