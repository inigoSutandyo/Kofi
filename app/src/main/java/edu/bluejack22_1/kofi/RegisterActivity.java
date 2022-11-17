package edu.bluejack22_1.kofi;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.bluejack22_1.kofi.controller.UserController;
import edu.bluejack22_1.kofi.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn;
    EditText eFullName, eEmail, ePassword, eRepeatPassword, eAddress;
    String FullName, Email, Password, RepeatPassword, Address;
    private FirebaseAuth mAuth;
    ActivityRegisterBinding binding;
    Uri ImageUri;
    ActivityResultLauncher<String> mImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        mImage = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>(){
            @Override
            public void onActivityResult(Uri result) {
                binding.registerProfileImage.setImageURI(result);
                ImageUri = result;
            }
        });
        eFullName = findViewById(R.id.txt_full_name);
        eEmail = findViewById(R.id.txt_email);
        ePassword = findViewById(R.id.txt_password);
        eRepeatPassword = findViewById(R.id.txt_repeat_password);
        eAddress = findViewById(R.id.txt_address);

        binding.registerProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImage.launch("image/*");
            }
        });

        registerBtn = findViewById(R.id.btn_register);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean check = ValidateInput();

                if(check){
                    RegisterAccount();
                } else{
                    Toast.makeText(RegisterActivity.this, "Error Bro",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean ValidateInput(){
        FullName = eFullName.getText().toString();
        Email = eEmail.getText().toString();
        Password = ePassword.getText().toString();
        RepeatPassword = eRepeatPassword.getText().toString();
        Address = eAddress.getText().toString();
        if(FullName.length() == 0){
            eFullName.setError("FullName must be filled");
            return false;
        } else if(Email.length() == 0){
            eEmail.setError("Email must be filled");
            return false;
        } else if(Password.length() < 6){
            ePassword.setError("Password must be more than 6 characters");
            return false;
        } else if(RepeatPassword == Password){
            eRepeatPassword.setError("Repeat password and password must be the same");
            return false;
        } else if(Address.length() == 0){
            eAddress.setError("Address must be filled");
            return false;
        }
        return true;
    }

    private void RegisterAccount(){
        UserController controller = new UserController();
        mAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "User Registered",
                                    Toast.LENGTH_LONG).show();
                            mAuth.getCurrentUser().sendEmailVerification();
                            String uid = mAuth.getUid();
                            controller.addUser(FullName, Email, Password, Address, ImageUri, uid);
                            MoveLoginPage();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void MoveLoginPage(){
        Intent LoginIntent = new Intent(this, LoginActivity.class);
        startActivity(LoginIntent);
    }
}