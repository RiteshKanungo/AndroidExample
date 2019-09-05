package com.path.socialintegration;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.path.socialintegration.Google.GoogleLoginActivity;
import com.path.socialintegration.facebook.CustomButtonFacebookActivity;

public class Controller extends AppCompatActivity {

    Button btn_facebook, btn_google;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_controller);
        findViews();
    }

    private void findViews() {
        btn_facebook = findViewById(R.id.btn_facebook);
        btn_google = findViewById(R.id.btn_google);

        btn_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, 0);
                startActivity(new Intent(Controller.this, CustomButtonFacebookActivity.class));
            }
        });

        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, 0);
                startActivity(new Intent(Controller.this, GoogleLoginActivity.class));
            }
        });
    }
}
