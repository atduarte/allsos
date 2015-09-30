package com.example.AllSOSservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SettingsActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    Hashtable<String,String> services;
    Hashtable<String,String> getIDbyName;
    Hashtable<String,String> myservices;
    Hashtable<String,CheckBox> dynamicCheckBoxes;

    double latitude;
    double longitude;
    String provider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        services = new Hashtable<String, String>();
        getIDbyName = new Hashtable<String, String>();
        myservices = new Hashtable<String, String>();
        dynamicCheckBoxes = new Hashtable<String,CheckBox>();
        ArrayList<String> items = new ArrayList<String>();
        ArrayList<String> currentServices = new ArrayList<String>();

        try {
            items = getPossibleServices();
            currentServices = getMyServices();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridLayout checkBoxesLayout = (GridLayout) findViewById(R.id.grid_checkboxes);
        initializeCheckBoxes(checkBoxesLayout,items);
        checkCurrentServices(checkBoxesLayout,currentServices);

        final Button submitChanges = (Button) findViewById(R.id.btn_submitChanges);
        final Button back = (Button) findViewById(R.id.btn_back_s);
        final Button sendLocation = (Button) findViewById(R.id.btn_local);

        final EditText newEmail = (EditText) findViewById(R.id.txt_newEmail);
        final EditText newNumber = (EditText) findViewById(R.id.txt_newTelNumber);
        final EditText newPassword = (EditText) findViewById(R.id.txt_newPassword);
        final EditText newConfirmPass = (EditText) findViewById(R.id.txt_newConfirmPassword);



        JSONObject fillInfo = null;
        try {
            fillInfo = getInfo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            newEmail.setText(fillInfo.get("email").toString());
            newNumber.setText(fillInfo.get("telephone").toString());
            newPassword.setText("");
            newConfirmPass.setText("");
            //newLocation.setText(fillInfo.get("location").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                MyLocation.LocationResult locationResult = new MyLocation.LocationResult(){
                    @Override
                    public void gotLocation(Location loc){
                        latitude = loc.getLatitude();
                        longitude = loc.getLongitude();
                        provider = loc.getProvider();

                    }
                };
                MyLocation loc = new MyLocation();
                loc.getLocation(getApplicationContext(), locationResult);
                try {
                    Thread.sleep(300);
                    sendLocationInfo();

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

        submitChanges.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // chamada api pa adicionar
                String email = newEmail.getText().toString();
                String number = newNumber.getText().toString();
                String password = newPassword.getText().toString();
                String confirmPass = newConfirmPass.getText().toString();
                //String location = newLocation.getText().toString();
                ArrayList<String> newServices = new ArrayList<String>();

                for (String key : dynamicCheckBoxes.keySet()) {
                    if((dynamicCheckBoxes.get(key)).isChecked()){
                        newServices.add(getIDbyName.get(key));
                    }
                }

                try {
                    if(submitChanges(email,number,password,confirmPass,newServices)){
                        msgBox_okbuttononly_goto_loggedin("Sucesso", "Alterações efetuadas com sucesso");
                    }
                    else{
                        msgBox_okbuttononly_stay("Erro", "Não foi possível efectuar as alterações. Por favor verifique os campos inseridos");
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
                Intent intent = new Intent(SettingsActivity.this, LoggedInActivity.class);
                SettingsActivity.this.startActivity(intent);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, LoggedInActivity.class);
        SettingsActivity.this.startActivity(intent);
        finish();
    }

    JSONObject getInfo() throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = "user/getinfo?token=" + UserInformation.token + "&email="+ UserInformation.email;

        APICall a = new APICall(url);
        return a.getJson();
    }

    boolean submitChanges(String email, String number, String pw, String conf_pw, ArrayList<String> selectedService) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        if(!pw.equals(conf_pw)){
            return false;
        }
        else{
            String url = "user/changeinfo?"
                    + "token="+ UserInformation.token
                    + "&email=" + email
                    + "&telephone=" + number;
            if(!pw.equals("")){
                url+= "&password=" + pw;
            }
            for(int i = 0; i < selectedService.size(); i++){
                url+= "&services[]=" + selectedService.get(i);
            }

            APICall a = new APICall(url);
            JSONObject res = a.getJson();

            String success = res.get("success").toString();
            if(success.equals("true")){
                UserInformation.email = email;
                return true;
            }
            else{
                return false;
            }
        }
    }

    public void msgBox_okbuttononly_goto_loggedin(String str, String str2)
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
                Intent intent = new Intent(SettingsActivity.this, LoggedInActivity.class);
                SettingsActivity.this.startActivity(intent);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void msgBox_okbuttononly_stay(String str, String str2)
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
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public ArrayList<String> getMyServices() throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        ArrayList<String> res = new ArrayList<String>();
        String url = "user/getinfo?token=" + UserInformation.token + "&email=" + UserInformation.email;
        APICall a = new APICall(url);

        JSONObject json_obj = a.getJson();
        JSONArray arr = json_obj.getJSONArray("services");
        String id;
        String name;
        for(int i = 0; i < arr.length(); i++){
            id = arr.get(i).toString();
            name = services.get(id);
            if(id.equals("null")) continue;
            myservices.put(id, name);
            res.add(name);
        }
        return res;
    }
    public ArrayList<String> getPossibleServices() throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        ArrayList<String> aux = new ArrayList<String>();
        String url = "service/list";
        APICall a = new APICall(url);
        JSONArray arr = a.getJsonArray();
        int j= 0;
        for(int i = 0; i < arr.length(); i++){
            JSONObject curr = new JSONObject(arr.get(i).toString());
            String cat = curr.getString("name").toString();
            aux.add(cat);
            JSONObject obj = (JSONObject) curr.get("_id");
            String id = obj.getString("$id").toString();
            services.put(id,cat);
            getIDbyName.put(cat,id);
        }
        return aux;
    }

    public void initializeCheckBoxes(GridLayout ll, ArrayList<String> items){
        for(int i = 0; i < items.size(); i++) {
            final CheckBox myBox = new CheckBox(this);
            myBox.setId(i);
            myBox.setText(items.get(i)); // Devolvido pela API
            myBox.setTextColor(Color.GRAY);
            ll.addView(myBox);
            dynamicCheckBoxes.put(items.get(i), myBox);
        }
    }

    public void checkCurrentServices(GridLayout ll, ArrayList<String> items){
        for(int i = 0; i < items.size(); i++){
            (dynamicCheckBoxes.get(items.get(i))).setChecked(true);
        }
    }

    public boolean sendLocationInfo() throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = "user/changelocation?token=" + UserInformation.token + "&email=" + UserInformation.email
                + "&lat=" + latitude
                + "&lon=" + longitude;
        APICall a = new APICall(url);

        JSONObject res = a.getJson();
        String success = res.getString("success");

        if(success.equals("true")){
            Toast toast = Toast.makeText(this, "Localização atualizada com sucesso.", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }
        else{
            String message = res.getString("message");
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.show();
            return false;
        }
    }

}
