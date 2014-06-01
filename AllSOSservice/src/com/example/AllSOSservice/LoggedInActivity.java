package com.example.AllSOSservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class LoggedInActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    Hashtable<String,String> getIDbyName;
    Hashtable<String,String> services;
    Hashtable<String,String> myservices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        getIDbyName = new Hashtable<String, String>();
        services = new Hashtable<String, String>();
        myservices = new Hashtable<String, String>();

        final TextView userEmail = (TextView) findViewById(R.id.lbl_username);
        final TextView telephone = (TextView) findViewById(R.id.lbl_telephone_w);
        final TextView location = (TextView) findViewById(R.id.lbl_location);

        JSONObject fillInfo = null;
        try {
            fillInfo = getInfo();
            userEmail.setText(fillInfo.get("email").toString());
            telephone.setText(fillInfo.get("telephone").toString());
            location.setText("");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Button goToSettings = (Button) findViewById(R.id.btn_gotosettings);
        final Button logout = (Button) findViewById(R.id.btn_logout);

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

        LinearLayout servicesOn = (LinearLayout) findViewById(R.id.ll_avaiable);
        for(int i = 0; i < currentServices.size(); i++){
            TextView aux = new TextView(this);
            aux.setText(currentServices.get(i));
            aux.setTextSize(TypedValue.COMPLEX_UNIT_PT,12);
            servicesOn.addView(aux);
        }

        goToSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, SettingsActivity.class);
                LoggedInActivity.this.startActivity(intent);
                finish();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoggedInActivity.this, LoginActivity.class);
                LoggedInActivity.this.startActivity(intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        msgBox_logout("Deseja voltar para o ecrã inicial?");
    }


    public void msgBox_logout(String str)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(str);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(LoggedInActivity.this, LoginActivity.class);
                LoggedInActivity.this.startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    JSONObject getInfo() throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = "user/getinfo?token=" + UserInformation.token + "&email="+ UserInformation.email;

        APICall a = new APICall(url);
        return a.getJson();
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
            getIDbyName.put(name,id);
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
        }
        return aux;
    }
}
