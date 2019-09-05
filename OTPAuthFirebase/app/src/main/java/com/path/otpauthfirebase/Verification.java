package com.path.otpauthfirebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verification extends AppCompatActivity {

    private EditText edt_otp;
    private TextView txt_time;
    private Button btn_resend, btn_sign_in;
    private SharedPreferences sharedPreferences;
    private String mobile_no, otp;
    private FirebaseAuth auth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        mobile_no = sharedPreferences.getString("mobileNo", "N/A");
        Log.e("Mobile", mobile_no);

        findView();

        StartFirebaseLogin();
        sendOTP();

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    otp = edt_otp.getText().toString();
                    if (otp != null && otp.length() != 0) {


                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                        SigninWithPhone(credential);
                    }
                } catch (Exception e) {
                    Log.e("Exeption", e + "");
                }
            }
        });

        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP();
            }
        });
    }

    private void sendOTP() {
        try {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    mobile_no,                        // Phone number to verify
                    60,                            // Timeout duration
                    TimeUnit.SECONDS,                // Unit of timeout
                    Verification.this,        // Activity (for callback binding)
                    mCallback);
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(Verification.this, SignIn.class));
                            finish();
                        } else {
                            showInfoToUser(task);
                            Toast.makeText(Verification.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SetTimer() {

        new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                txt_time.setText("seconds remaining: " + millisUntilFinished / 1000);
                btn_resend.setEnabled(false);
            }

            public void onFinish() {
                btn_resend.setEnabled(true);
                txt_time.setText("");
            }

        }.start();
    }

    private void StartFirebaseLogin() {

        try {
            auth = FirebaseAuth.getInstance();
            mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    Toast.makeText(Verification.this, "verification completed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(Verification.this, "verification failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationCode = s;
                    Toast.makeText(Verification.this, "Code sent", Toast.LENGTH_SHORT).show();
                    SetTimer();
                }
            };
        } catch (Exception e) {
            Log.e("Exception", e + "");
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                edt_otp.setText(message);
            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void findView() {
        edt_otp = findViewById(R.id.edt_otp);
        btn_resend = findViewById(R.id.btn_resend);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        txt_time = findViewById(R.id.txt_time);
    }

    private void showInfoToUser(Task<AuthResult> task) {
        //here manage the exceptions and show relevant information to user
        //hideProgressDialog();
        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "user_already_exist_msg", Toast.LENGTH_SHORT).show();

        } else if (task.getException() instanceof FirebaseAuthWeakPasswordException) {
            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
            //invalid phone /otp
            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
