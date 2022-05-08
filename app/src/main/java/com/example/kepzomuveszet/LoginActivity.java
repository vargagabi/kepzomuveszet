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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG= LoginActivity.class.getName()+"logcat";
    private static final String PREF_KEY=LoginActivity.class.getPackage().toString();
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

        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);


        //firebase adattagok
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("email",loginEmailET.getText().toString());
        editor.putString("password", loginPasswordET.getText().toString());
        editor.apply();
    }

    public void onLoginAccept(View view) {
        String email = loginEmailET.getText().toString().trim();
        String password = loginPasswordET.getText().toString();
        if(!email.isEmpty() && !password.isEmpty()){
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this,"Sikeres bejelentkezés",Toast.LENGTH_LONG).show();
                        setResult(2);
                        finish();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this,"Sikertelen bejelentkezés: " + task.getException().getMessage(),Toast.LENGTH_LONG);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this,"Error: " + e,Toast.LENGTH_LONG);
                }
            });
        }
    }

    public void onLoginCancel(View view) {
        finish();
    }

    public void onLoginRegister(View view) {
        startActivity(new Intent(this,RegisterActivity.class));
    }
}