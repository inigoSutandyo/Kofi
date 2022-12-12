package edu.bluejack22_1.kofi;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import edu.bluejack22_1.kofi.controller.UserController;
import edu.bluejack22_1.kofi.model.Notification;
import edu.bluejack22_1.kofi.model.User;


public class LoginActivity extends AppCompatActivity {
    private Button loginBtn;
    private TextView registerTxt;
    private EditText eEmail, ePassword;
    private String email, password;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private ImageView googleBtn;
    private FirebaseFirestore db;
    private UserController usercontroller;
    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();



        usercontroller = new UserController();
        googleBtn = findViewById(R.id.google_btn);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        gsc = GoogleSignIn.getClient(this, gso);


        registerTxt = findViewById(R.id.txt_sign_up);
        loginBtn = findViewById(R.id.btn_login);
        eEmail = findViewById(R.id.txt_login_email);
        ePassword = findViewById(R.id.txt_login_password);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel("WELCOME_NOTIFICATION", "WELCOME", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        if(mAuth.getCurrentUser() != null){
            db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String fullName = (String) document.getData().get("fullName");
                            String email = (String) document.getData().get("email");
                            String address = (String) document.getData().get("address");
                            String password = (String) document.getData().get("password");
                            String role = (String) document.getData().get("role");
                            String image = (String) document.getData().get("imageUrl");
                            User.setCurrentUser(new User(fullName, email, password, address, role, document.getId(), image));
                            moveMainPage();
                        }
                    }
                }
            });
        }
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 googleLogin();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
        registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveRegisterPage();
            }
        });
    }


    private void googleLogin(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginWithGoogleAuth(account);
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void loginWithGoogleAuth(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Activity currActivity = this;
        mAuth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                currentUser = authResult.getUser();
                db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(!doc.exists()){
                                usercontroller.addGoogleUser(currentUser.getUid(), currentUser.getDisplayName(),
                                        currentUser.getEmail(), currentUser.getPhotoUrl().toString(),currActivity);
                            } else{
                                User user = new User((String) doc.getData().get("fullName"),
                                        (String) doc.getData().get("email"),
                                        "",
                                        (String) doc.getData().get("password"),
                                        "User",
                                        doc.getId(),
                                        (String)doc.getData().get("imageUrl"));
                                welcomeNotification();
                                User.setCurrentUser(user);
                                moveMainPage();
                            }
                        }
                    }
                });
            }
        });
    }


    private void loginUser(){
        email = eEmail.getText().toString();
        password = ePassword.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            db.collection("users").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String fullName = (String) document.getData().get("fullName");
                                            String email = (String) document.getData().get("email");
                                            String address = (String) document.getData().get("address");
                                            String password = (String) document.getData().get("password");
                                            String role = (String) document.getData().get("role");
                                            welcomeNotification();
                                            User.setCurrentUser(new User(fullName, email, password, address, role, document.getId(), ""));
                                            finish();
                                            moveMainPage();
                                        }
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            eEmail.setError("Failed Login");
                        }
                    }
                });
    }

    private void welcomeNotification() {
        notificationManager = NotificationManagerCompat.from(LoginActivity.this);
        builder = new NotificationCompat.Builder(this, "WELCOME_NOTIFICATION")
                .setSmallIcon(R.drawable.kofi)
                .setContentTitle("WELCOME")
                .setContentText("Welcome to KoFi!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        notificationManager.notify(1, builder.build());
    }

    private void moveRegisterPage(){
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
    }
    private void moveMainPage(){
        finish();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}