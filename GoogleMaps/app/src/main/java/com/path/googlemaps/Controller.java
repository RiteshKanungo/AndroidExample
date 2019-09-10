package com.path.googlemaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.path.googlemaps.AnimateMovingMarker.MarkerActivity;
import com.path.googlemaps.MarkerAnimation.Activity;
import com.path.googlemaps.MultipleMarker.MainActivity;
import com.path.googlemaps.NearPlacesinfo.NearByPlaces;

public class Controller extends AppCompatActivity {

    private Button btn_current_location, btn_nearByPlaces, btn_Animate_marker,btn_moving_marker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_controller);
        findViews();
    }

    private void findViews() {
        btn_current_location = findViewById(R.id.btn_current_location);
        btn_nearByPlaces = findViewById(R.id.btn_nearByPlaces);
        btn_Animate_marker = findViewById(R.id.btn_Animate_marker);
        btn_moving_marker = findViewById(R.id.btn_moving_marker);

        btn_current_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Controller.this, MainActivity.class));
                overridePendingTransition(0, 0);
            }
        });

        btn_nearByPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Controller.this, NearByPlaces.class));
                overridePendingTransition(0, 0);
            }
        });

        btn_Animate_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Controller.this, Activity.class));
                overridePendingTransition(0,0);
            }
        });

        btn_moving_marker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Controller.this, MarkerActivity.class));
                overridePendingTransition(0,0);
            }
        });
    }
}
