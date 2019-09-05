package com.path.otpauthfirebase;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignUp extends AppCompatActivity {

    private EditText edt_name, edt_mail, edt_pass, edt_confirm_pass, edt_mobile;
    private Button btn_confirm;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        findViews();
        checkAndRequestPermissions();
        sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    private void findViews() {

        edt_name = findViewById(R.id.edt_name);
        edt_mail = findViewById(R.id.edt_mail);
        edt_pass = findViewById(R.id.edt_pass);
        edt_confirm_pass = findViewById(R.id.edt_confirm_pass);
        edt_mobile = findViewById(R.id.edt_mobile);
        btn_confirm = findViewById(R.id.btn_confirm);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeInLocalDB();
                overridePendingTransition(0, 0);
                startActivity(new Intent(SignUp.this, Verification.class));
                finish();
            }
        });
    }

    private void storeInLocalDB() {

        editor.putString("name", edt_name.getText().toString().trim());
        editor.putString("email", edt_mail.getText().toString().trim());
        editor.putString("password", edt_pass.getText().toString().trim());
        editor.putString("confirmpassword", edt_confirm_pass.getText().toString().trim());
        editor.putString("mobileNo", edt_mobile.getText().toString().trim());
        editor.commit();
    }

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_MMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;


    }

}
