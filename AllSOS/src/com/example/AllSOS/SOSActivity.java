package com.example.AllSOS;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SOSActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    String selectedService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.askhelp);

        final TextView labelSelectedService = (TextView)findViewById(R.id.lbl_selected);

        final Button sos = (Button) findViewById(R.id.btn_sos);
        final Button back = (Button) findViewById(R.id.btn_back);


        sos.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(askForHelp(selectedService)){
                    // que fazer a seguir?
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.main);
            }
        });


    }

    boolean askForHelp(String service){
        // Chamada API para pedir Ajuda
        return true;
    }

}
