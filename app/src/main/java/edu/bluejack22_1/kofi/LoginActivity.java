package edu.bluejack22_1.kofi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    TextView registerTxt;
    EditText eEmail, ePassword;
    String email, password;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        registerTxt = findViewById(R.id.txt_sign_up);
        loginBtn = findViewById(R.id.btn_login);
        eEmail = findViewById(R.id.txt_login_email);
        ePassword = findViewById(R.id.txt_login_password);
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });
        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MoveRegisterPage();
            }
        });
    }

    private void LoginUser(){
        email = eEmail.getText().toString();
        password = ePassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            MoveMainPage();
                        } else {
                            // If sign in fails, display a message to the user.

                        }
                    }
                });
    }


    private void MoveRegisterPage(){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
    private void MoveMainPage(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}