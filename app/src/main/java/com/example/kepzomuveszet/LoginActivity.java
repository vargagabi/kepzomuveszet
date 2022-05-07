package com.example.kepzomuveszet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmailET;
    EditText loginPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginEmailET = findViewById(R.id.loginEmailEditText);
        loginPasswordET = findViewById(R.id.loginPasswordEditText);
    }

    public void onLoginAccept(View view) {
    }

    public void onLoginCancel(View view) {
        finish();
    }
}