package com.example.AllSOS;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        final Button register = (Button) findViewById(R.id.btn_register);
        final EditText eEmail = (EditText)findViewById(R.id.txt_email);
        final EditText ePassword = (EditText)findViewById(R.id.txt_email);
        final EditText eConfirmPassword = (EditText)findViewById(R.id.txt_confirm);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String insertedEmail = eEmail.getText().toString();
                String insertedPassword = ePassword.getText().toString();
                String insertedConfirmPassword = eConfirmPassword.getText().toString();

                if(register_areInputsValid(insertedEmail, insertedPassword, insertedConfirmPassword)){
                    if(registerUser(insertedEmail, insertedPassword, insertedConfirmPassword)){
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    boolean register_areInputsValid(String email, String password, String confirmPassword){
        if(password != confirmPassword){
            //TODO: alterar para false
            return true;
        }
        // Verificar se email ja existe (?) Chamada API
        return true;
    }

    boolean registerUser(String email, String password, String confirmPassword){
        // Chamada API para registo
        return true;
    }


}
