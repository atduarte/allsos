package com.example.AllSOS;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    String selectedService = "empty";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final Button register = (Button) findViewById(R.id.btn_register);
        final EditText eEmail = (EditText)findViewById(R.id.txt_email);
        final EditText ePassword = (EditText)findViewById(R.id.txt_email);
        final EditText eConfirmPassword = (EditText)findViewById(R.id.txt_confirm);

        final Button signIn = (Button) findViewById(R.id.btn_signin);
        final EditText sEmail = (EditText)findViewById(R.id.txt_s_email);
        final EditText sPassword = (EditText)findViewById(R.id.txt_s_email);


        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String insertedEmail = eEmail.getText().toString();
                String insertedPassword = ePassword.getText().toString();
                String insertedConfirmPassword = eConfirmPassword.getText().toString();

                if(register_areInputsValid(insertedEmail, insertedPassword, insertedConfirmPassword)){
                    if(registerUser(insertedEmail, insertedPassword, insertedConfirmPassword)){
                        setContentView(R.layout.login);
                    }
                }
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String insertedEmail = sEmail.getText().toString();
                String insertedPassword = sPassword.getText().toString();

                if(signInUser(insertedEmail, insertedPassword)){
                    setContentView(R.layout.main);
                    initializeServiceButtons();
                }
            }
        });


    }

    // API

    boolean register_areInputsValid(String email, String password, String confirmPassword){
        if(password != confirmPassword){
            return false;
        }
        // Verificar se email ja existe (?) Chamada API
        return true;
    }

    boolean registerUser(String email, String password, String confirmPassword){
        // Chamada API para registo
        return true;
    }

    boolean signInUser(String insertedEmail, String insertedPassword){
        // Chamada API para Login
        return true;
    }

    // Inicializacoes

    void initializeServiceButtons(){
        //Chamada API para saber os servicos
        int n = 5;
        for(int i = 0; i < n; i++){
            final Button myButton = new Button(this);
            myButton.setId(i);
            myButton.setText("Change Service Name"); // Devolvido pela API
            myButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    selectedService = myButton.getText().toString();
                    setContentView(R.layout.askhelp);
                }
            });

        }
    }

}
