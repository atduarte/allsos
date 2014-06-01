package com.example.AllSOS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SOSActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    TextView locationLabel;
    String locationText;
    double latitude;
    double longitude;
    String provider;
    String idService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.askhelp);

        final TextView serviceSelected = (TextView)findViewById(R.id.lbl_service);

        final Button sos = (Button) findViewById(R.id.btn_sos);
        final Button back = (Button) findViewById(R.id.btn_back);

        Intent intent = getIntent();
        String s = intent.getStringExtra("SelectedService");
        idService = intent.getStringExtra("SelectedServiceID");
        serviceSelected.setText(s);

        //locationLabel = (TextView) findViewById(R.id.lbl_location_sos);

        MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
            @Override
            public void gotLocation(Location loc){
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                provider = loc.getProvider();

                locationText = "Lat: " + loc.getLatitude()
                        + "\nLong: " + loc.getLongitude();

                //locationLabel.setText(locationText);
            }
        };
        MyLocation loc = new MyLocation();
        loc.getLocation(this, locationResult);

        sos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //refreshLocationOnServer();
                    if(askForHelp(idService)){
                        msgBox_okbuttononly_goto_main("Sucesso", "Pedido efectuado com sucesso");
                        //Toast toast = Toast.makeText(getBaseContext(), "Pedido efectuado com sucesso", 1000);
                        //toast.show();
                    }
                    else{
                        msgBox_okbuttononly_goto_main("Erro", "Não foi possível terminar o pedido");
                        //Toast toast = Toast.makeText(getBaseContext(), "Erro no pedido", 1000);
                        //toast.show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }

    boolean askForHelp(String service) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = "call/make?token=" + UserInformation.token + "&email=" + UserInformation.email
                + "&lat=" + latitude
                + "&lon=" + longitude
                + "&service=" + service;
        APICall a = new APICall(url);

        JSONObject res = a.getJson();
        String success = res.getString("success");

        if(success.equals("true")){
            return true;
        }
        else{
            return false;
        }
    }

    boolean refreshLocationOnServer() throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = "user/changelocation?token=" + UserInformation.token + "&email=" + UserInformation.email
                + "&lat=" + latitude
                + "&lon=" + longitude;
        APICall a = new APICall(url);
        JSONObject res = a.getJson();
        String success = res.getString("success");

        if(success.equals("true")){
            return true;
        }
        else{
            return false;
        }

    }

    public void msgBox_okbuttononly_goto_main(String str, String str2)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(SOSActivity.this, SelectServiceActivity.class);
                SOSActivity.this.startActivity(intent);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
