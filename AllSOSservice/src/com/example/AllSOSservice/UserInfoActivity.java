package com.example.AllSOSservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class UserInfoActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientinfo);

        Intent intent = getIntent();
        String reqEmail = intent.getStringExtra("email");
        String reqTelephone = intent.getStringExtra("number");
        String reqLatitude = intent.getStringExtra("latitude");
        String reqLongitude = intent.getStringExtra("longitude");
        String reqService = intent.getStringExtra("service");

        TextView lblEmail = (TextView) findViewById(R.id.lbl_reqEmail);
        TextView lblTelephone = (TextView) findViewById(R.id.lbl_reqTelephone);
        TextView lblLatitude = (TextView) findViewById(R.id.lbl_reqLatitude);
        TextView lblLongitude = (TextView) findViewById(R.id.lbl_reqLongitude);
        TextView lblService = (TextView) findViewById(R.id.lbl_reqService);

        lblEmail.setText(reqEmail);
        lblTelephone.setText(reqTelephone);
        lblLatitude.setText(reqLatitude);
        lblLongitude.setText(reqLongitude);
        lblService.setText(reqService);

        Button back = (Button) findViewById(R.id.btn_back_cinfo);
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, LoggedInActivity.class);
                UserInfoActivity.this.startActivity(intent);
                finish();
            }
        });

        GoogleMap map = ((MapFragment) getFragmentManager()
                .findFragmentById(R.id.map)).getMap();

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(Double.parseDouble(reqLatitude), Double.parseDouble(reqLatitude)), 16));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        map.addMarker(new MarkerOptions()
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.house_flag))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(41.889, -87.622)));


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserInfoActivity.this, LoggedInActivity.class);
        UserInfoActivity.this.startActivity(intent);
        finish();
    }
}
