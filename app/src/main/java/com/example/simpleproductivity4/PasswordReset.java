package com.example.simpleproductivity4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class PasswordReset extends AppCompatActivity implements View.OnClickListener {


    private TextView password_reset_back_to_login, password_reset_button;
    private EditText password_reset_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        password_reset_back_to_login = findViewById(R.id.password_reset_back_to_login);
        password_reset_back_to_login.setOnClickListener(this);

        password_reset_button = findViewById(R.id.password_reset_button);
        password_reset_button.setOnClickListener(this);
        password_reset_email = findViewById(R.id.password_reset_email);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.password_reset_back_to_login:
                startActivity(new Intent(this, LoginScreen.class));
                finish();
                break;
            case R.id.password_reset_button:
                resetPassword();
                break;
        }
    }

    private void resetPassword() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String emailAddress = password_reset_email.getText().toString().trim();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("reset", "Email sent.");
                            Toast.makeText(PasswordReset.this, "Email has been sent",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}