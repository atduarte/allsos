package com.example.AllSOS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        final Button signIn = (Button) findViewById(R.id.btn_signin);
        final EditText sEmail = (EditText)findViewById(R.id.txt_s_email);
        final EditText sPassword = (EditText)findViewById(R.id.txt_s_email);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String insertedEmail = sEmail.getText().toString();
                String insertedPassword = sPassword.getText().toString();

                if(signInUser(insertedEmail, insertedPassword)){
                    Intent intent = new Intent(LoginActivity.this, SelectServiceActivity.class);
                    LoginActivity.this.startActivity(intent);
                }
            }
        });

    }

    // API
    boolean signInUser(String insertedEmail, String insertedPassword){
        // Chamada API para Login
        return true;
    }
}
