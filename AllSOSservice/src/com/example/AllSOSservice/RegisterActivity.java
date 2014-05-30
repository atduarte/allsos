package com.example.AllSOSservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        final Button back = (Button) findViewById(R.id.btn_back_r);
        final Button register = (Button) findViewById(R.id.btn_register_r);
        final EditText eTelephone = (EditText)findViewById(R.id.txt_telNumber);
        final EditText eEmail = (EditText)findViewById(R.id.txt_email);
        final EditText ePassword = (EditText)findViewById(R.id.txt_password);
        final EditText eConfirmPassword = (EditText)findViewById(R.id.txt_confirmPassword);


        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String insertedEmail = eEmail.getText().toString();
                String insertedPassword = ePassword.getText().toString();
                String insertedConfirmPassword = eConfirmPassword.getText().toString();
                String insertedTelephone = eTelephone.getText().toString();


                if(register_areInputsValid(insertedEmail, insertedPassword, insertedConfirmPassword)){

                    try {
                        if(registerUser(insertedEmail, insertedPassword, insertedTelephone)){
                            msgbox_okbuttononly_gotologin("Sucesso", "O utilizador "+ insertedEmail +" foi registado com sucesso.");
                        }
                        else{
                            msgBox_okbuttononly_stay("Erro", "Não foi possível concluir o registo.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                }
                else{
                    msgBox_okbuttononly_stay("Valores Inválido", "Valores inseridos são inválidos.");
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
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
        builder.setMessage(str);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
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

    boolean register_areInputsValid(String email, String password, String confirmPassword){
        if(password != confirmPassword){
            //TODO: alterar para false
            return true;
        }
        // Verificar se email ja existe (?) Chamada API
        return true;
    }

    boolean registerUser(String email, String password, String telephone) throws IOException, JSONException, InterruptedException, ExecutionException, TimeoutException {
        // Chamada API para registo
        String addToUrl = "user/signup?email=%22" + email + "%22&password=%22" + password + "%22&telephone=%22" + telephone + "%22";

        APICall a = new APICall(addToUrl);
        JSONObject res = a.getJson();
        String success = res.get("success").toString();
        if(success.equals("true")){
            return true;
        }
        else
            return false;

    }

    public void msgBox_okbuttononly_stay(String str, String str2)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void msgbox_okbuttononly_gotologin(String str, String str2)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(str);
        builder.setMessage(str2);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }



}
