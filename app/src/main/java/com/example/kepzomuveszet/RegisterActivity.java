package com.example.kepzomuveszet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG=MainActivity.class.getName()+"logcat";
    EditText registerEmailET;
    EditText registerPasswordET;
    EditText registerPasswordAgainET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
    }

    public void onRegisterCancel(View view) {
        startActivity(new Intent(this,));
    }

    public void onRegisterAccept(View view) {
        registerEmailET = findViewById(R.id.registerEmailEditText);
        registerPasswordET = findViewById(R.id.registerPasswordEditText);
        registerPasswordAgainET =findViewById(R.id.registerPasswordAgainEditText);

        Log.i(LOG_TAG,registerEmailET.getText().toString()+", "+registerPasswordET.getText().toString()+", "+registerPasswordAgainET.getText().toString());
    }
}