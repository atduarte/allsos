package com.example.AllSOS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import java.util.ArrayList;

public class SelectServiceActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    String selectedService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);

        final LinearLayout ll;
        ll = (LinearLayout) findViewById(R.id.linearLayout);


        initializeServiceButtons(ll);

    }

    // API
    void initializeServiceButtons(LinearLayout ll){
        //Chamada API para saber os servicos

        ArrayList<String> categorias = new ArrayList<String>();
        categorias.add("Medico");
        categorias.add("Canalizador");
        categorias.add("Veterinario");
        categorias.add("Florista");
        categorias.add("Cangalheiro");

        /*
        final Button myButton = new Button(this);
        myButton.setId(1);
        myButton.setText("teste");
        myButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectedService = myButton.getText().toString();
                Intent myIntent = new Intent(SelectServiceActivity.this, SOSActivity.class);
                //myIntent.putExtra("SelectedService", selectedService);
                SelectServiceActivity.this.startActivity(myIntent);
            }
        });
        ll.addView(myButton);



        */
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
}
