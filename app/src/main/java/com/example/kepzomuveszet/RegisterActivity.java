package com.example.kepzomuveszet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private static final String LOG_TAG=MainActivity.class.getName()+"logcat";
    private static final String PREF_KEY=MainActivity.class.getPackage().toString()
            ;
    EditText registerEmailET;
    EditText registerPasswordET;
    EditText registerPasswordAgainET;

    private FirebaseAuth firebaseAuth;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //adattagok kezelese
        registerEmailET = findViewById(R.id.registerEmailEditText);
        registerPasswordET = findViewById(R.id.registerPasswordEditText);
        registerPasswordAgainET =findViewById(R.id.registerPasswordAgainEditText);

        preferences = getSharedPreferences(PREF_KEY,MODE_PRIVATE);
        registerEmailET.setText(preferences.getString("email",""));
        registerPasswordET.setText(preferences.getString("password",""));

        //firebase adattagok
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void onRegisterCancel(View view) {
        finish();
    }

    public void onRegisterAccept(View view) {
        String email = registerEmailET.getText().toString();
        String password = registerPasswordET.getText().toString();
        String passwordA = registerPasswordAgainET.getText().toString();
        if(!email.isEmpty() && password.equals(passwordA)){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Sikeres regisztráció, kérlek jelentkezz be",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,"Sikertelen regisztráció: " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        Log.i(LOG_TAG,registerEmailET.getText().toString()+", "+registerPasswordET.getText().toString()+", "+registerPasswordAgainET.getText().toString());
    }
}