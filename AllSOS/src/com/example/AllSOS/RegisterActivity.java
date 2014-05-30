package com.example.AllSOS;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RegisterActivity extends Activity {
    TextView resposta;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        resposta = (TextView) findViewById(R.id.lbl_numerotele);

        final Button register = (Button) findViewById(R.id.btn_register);
        final EditText eEmail = (EditText)findViewById(R.id.txt_email);
        final EditText ePassword = (EditText)findViewById(R.id.txt_email);
        final EditText eConfirmPassword = (EditText)findViewById(R.id.txt_confirm);

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    testCall();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }


                String insertedEmail = eEmail.getText().toString();
                String insertedPassword = ePassword.getText().toString();
                String insertedConfirmPassword = eConfirmPassword.getText().toString();


                if(register_areInputsValid(insertedEmail, insertedPassword, insertedConfirmPassword)){
                    if(registerUser(insertedEmail, insertedPassword, insertedConfirmPassword)){
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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

     void testCall() throws JSONException, InterruptedException, ExecutionException, TimeoutException {
         APICall a = new APICall("http://hmkcode.appspot.com/rest/controller/get.json");
         String o = a.getResponse();
         resposta.setText(o);


     }





}
