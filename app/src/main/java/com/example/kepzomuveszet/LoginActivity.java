package com.example.kepzomuveszet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText loginEmailET;
    EditText loginPasswordET;

    private SharedPreferences preferences;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginEmailET = findViewById(R.id.loginEmailEditText);
        loginPasswordET = findViewById(R.id.loginPasswordEditText);

        //firebase adattagok
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onLoginAccept(View view) {
        String email = loginEmailET.getText().toString();
        String password = loginPasswordET.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    //TODO:shared preferencesbe az emailt eltarolni mert be van jelentkezve egy user?
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Sikeres bejelentkezes",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this,"Failed to log in: " + task.getException().getMessage(),Toast.LENGTH_LONG);
                    }
                }
            });
        }
    }

    public void onLoginCancel(View view) {
        finish();
    }
}