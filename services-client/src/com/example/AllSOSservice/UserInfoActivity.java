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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
        final String reqLatitude = intent.getStringExtra("latitude");
        final String reqLongitude = intent.getStringExtra("longitude");
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
        /*
        Button gotoMap = (Button) findViewById(R.id.btn_gotomap);
        gotoMap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, ShowMapActivity.class);
                intent.putExtra("latitude", reqLatitude);
                intent.putExtra("longitude", reqLongitude);
                UserInfoActivity.this.startActivity(intent);
                finish();
            }
        });
        */
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UserInfoActivity.this, LoggedInActivity.class);
        UserInfoActivity.this.startActivity(intent);
        finish();
    }
}
