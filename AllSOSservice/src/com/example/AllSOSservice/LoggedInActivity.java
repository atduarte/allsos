package com.example.AllSOSservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.*;
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

    String emailTarget = null;
    String telephoneTarget = null;
    String latitudeTarget = null;
    String longitudeTarget = null;
    String serviceTarget = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        getIDbyName = new Hashtable<String, String>();
        services = new Hashtable<String, String>();
        myservices = new Hashtable<String, String>();


        JSONArray arr = null;
        try {
            arr = getCallsById();


            LinearLayout ll_requests = (LinearLayout) findViewById(R.id.ll_pedidos);
             /*
            TextView req_show_service = new TextView(this);
            req_show_service.setText("Serviço");
            TextView req_show_email = new TextView(this);
            req_show_email.setText("E-mail");
            TextView req_show_latitude = new TextView(this);
            req_show_latitude.setText("Latitude");
            TextView req_show_longitude = new TextView(this);
            req_show_longitude.setText("Longitude");

            ll_requests.addView(req_show_service);
            ll_requests.addView(req_show_email);
            ll_requests.addView(req_show_latitude);
            ll_requests.addView(req_show_longitude);
            */


            for(int i = 0; i < arr.length(); i++) {

                JSONObject obj = arr.getJSONObject(i);
                JSONObject userObj = obj.getJSONObject("user");

                emailTarget = userObj.getString("email").toString();
                TextView req_email = new TextView(this);
                req_email.setText("E-mail: " + emailTarget);

                JSONArray location = userObj.getJSONArray("location");
                latitudeTarget = location.get(0).toString();
                TextView req_latitude = new TextView(this);
                req_latitude.setText("Latitude: " + latitudeTarget);
                longitudeTarget = location.get(1).toString();
                TextView req_longitude = new TextView(this);
                req_longitude.setText("Longitude: " + longitudeTarget);

                telephoneTarget = userObj.getString("telephone").toString();

                JSONObject call = obj.getJSONObject("call");
                JSONObject id1 = call.getJSONObject("_id");
                final String idS= id1.getString("$id");
                String serviceId = call.getString("service");
                serviceTarget = getServiceName(serviceId);
                TextView req_service = new TextView(this);
                req_email.setText("Serviço:" + serviceTarget);

                Button btnAccept = new Button(this);
                btnAccept.setText("Aceitar");

                btnAccept.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = "call/accept?token=" + UserInformation.token + "&email=" + UserInformation.email
                                + "&call=" + idS;
                        try {
                            APICall a = new APICall(url);
                            JSONObject obj = a.getJson();
                            int j = 0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                        Intent intent = new Intent(LoggedInActivity.this, UserInfoActivity.class);
                        intent.putExtra("email",emailTarget);
                        intent.putExtra("number",telephoneTarget);
                        intent.putExtra("latitude",latitudeTarget);
                        intent.putExtra("longitude",longitudeTarget);
                        intent.putExtra("service",serviceTarget);
                        LoggedInActivity.this.startActivity(intent);

                    }
                });
                Button btnReject = new Button(this);
                btnReject.setText("Rejeitar");
                btnReject.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        String url = "call/reject?token=" + UserInformation.token + "&email=" + UserInformation.email
                                + "&call=" + idS;
                        try {
                            APICall a = new APICall(url);
                            JSONObject obj = a.getJson();
                            int j = 0;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        finish();
                        Intent intent = new Intent(LoggedInActivity.this, LoggedInActivity.class);
                        LoggedInActivity.this.startActivity(intent);

                    }
                });

                ll_requests.addView(req_service);
                ll_requests.addView(req_email);
                ll_requests.addView(req_latitude);
                ll_requests.addView(req_longitude);
                ll_requests.addView(btnAccept);
                ll_requests.addView(btnReject);

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

        final TextView userEmail = (TextView) findViewById(R.id.lbl_username);
        final TextView telephone = (TextView) findViewById(R.id.lbl_telephone_w);
        //final TextView location = (TextView) findViewById(R.id.lbl_location);

        JSONObject fillInfo = null;
        try {
            fillInfo = getInfo();
            userEmail.setText(fillInfo.get("email").toString());
            telephone.setText(fillInfo.get("telephone").toString());
            //location.setText("");
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

        /*
        LinearLayout servicesOn = (LinearLayout) findViewById(R.id.ll_avaiable);
        for(int i = 0; i < currentServices.size(); i++){
            TextView aux = new TextView(this);
            aux.setText(currentServices.get(i));
            aux.setTextSize(TypedValue.COMPLEX_UNIT_PT,12);
            servicesOn.addView(aux);
        }
        */

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

    public JSONArray getCallsById() throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = "call/list?token=" + UserInformation.token + "&email=" + UserInformation.email;
        APICall a = new APICall(url);

        JSONArray arr = a.getJsonArray();
        int j = 0;
        return arr;
    }

    public String getServiceName(String id) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        String url = "service/list";

        APICall a = new APICall(url);
        JSONArray arr = a.getJsonArray();

        String res = null;
        for(int i = 0; i < arr.length(); i++){
            JSONObject curr = new JSONObject(arr.get(i).toString());
            JSONObject obj = (JSONObject) curr.get("_id");
            String gotID = obj.getString("$id").toString();
            if(gotID.equals(id)){
                res = curr.getString("name").toString();
                break;
            }
        }

        return res;
    }
}
