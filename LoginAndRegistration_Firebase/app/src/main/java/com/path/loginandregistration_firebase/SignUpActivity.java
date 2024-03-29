package com.path.loginandregistration_firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;


//https://www.androidhive.info/2016/06/android-getting-started-firebase-simple-login-registration-auth/
public class SignUpActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        findViews();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, 0);
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);

                    //create user
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        showInfoToUser(task);
                                    } else {
                                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                } catch (Exception e) {
                    Log.e("Exception", e + "");
                }
            }
        });
    }

    private void showInfoToUser(Task<AuthResult> task) {
        //here manage the exceptions and show relevant information to user
        //hideProgressDialog();
        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "Email Already Exist", Toast.LENGTH_SHORT).show();

        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
            //invalid phone /otp
            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void findViews() {
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignUp = findViewById(R.id.sign_up_button);
        btnResetPassword = findViewById(R.id.btn_reset_password);
    }

}
