package com.example.AllSOSservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoggedInActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiting);

        Intent intent = getIntent();
        String s = intent.getStringExtra("Username");

        final TextView userEmail = (TextView) findViewById(R.id.lbl_username);
        userEmail.setText(s);

        final Button goToSettings = (Button) findViewById(R.id.btn_gotosettings);
        final Button logout = (Button) findViewById(R.id.btn_logout);

        insertServices();

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

    public void insertServices(){
        //APICall a = new APICall("")
    }
}
