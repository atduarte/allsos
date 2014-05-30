package com.example.AllSOS;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SelectServiceActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    String selectedService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        final Button settings = (Button) findViewById(R.id.btn_gotosettings);
        final Button logout = (Button) findViewById(R.id.btn_logout);

        final LinearLayout ll;
        ll = (LinearLayout) findViewById(R.id.linearLayout);
        try {
            initializeServiceButtons(ll);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(SelectServiceActivity.this, SettingsActivity.class);
                SelectServiceActivity.this.startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent intent = new Intent(SelectServiceActivity.this, LoginActivity.class);
                SelectServiceActivity.this.startActivity(intent);
                finish();
            }
        });



    }

    @Override
    public void onBackPressed() {
        msgBox_logout("Deseja voltar para o ecrã inicial?");
    }

    // API
    void initializeServiceButtons(LinearLayout ll) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        //Chamada API para saber os servicos
        APICall a = new APICall("service/list");
        JSONArray arr = a.getJsonArray();

        ArrayList<String> categorias = new ArrayList<String>();
        for(int i = 0; i < arr.length(); i++){
            JSONObject curr = new JSONObject(arr.get(i).toString());
            String cat = curr.getString("name").toString();
            categorias.add(cat);
        }

        for(int i = 0; i < categorias.size(); i++){
            final Button myButton = new Button(this);
            myButton.setId(i);
            myButton.setText(categorias.get(i)); // Devolvido pela API
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectedService = myButton.getText().toString();
                    Intent myIntent = new Intent(SelectServiceActivity.this, SOSActivity.class);
                    myIntent.putExtra("SelectedService", selectedService);
                    SelectServiceActivity.this.startActivity(myIntent);
                }
            });
            ll.addView(myButton);
        }

    }

    public void msgBox_logout(String str)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(str);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SelectServiceActivity.this, LoginActivity.class);
                SelectServiceActivity.this.startActivity(intent);
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
}
