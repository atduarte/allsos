package com.example.AllSOSservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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
        final Button lRegister = (Button) findViewById(R.id.btn_gotoreg);

        signIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String insertedEmail = sEmail.getText().toString();
                String insertedPassword = sPassword.getText().toString();


                try {
                    if(signInUser(insertedEmail, insertedPassword)){
                        Intent intent = new Intent(LoginActivity.this, LoggedInActivity.class);
                        intent.putExtra("Username", insertedEmail);
                        LoginActivity.this.startActivity(intent);
                        finish();
                    }
                    else{
                        msgBox_okbuttononly("Erro", "Não foi possível autenticar o utilizador. Por favor verifique as suas credenciais");
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
        });

        lRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        });

    }

    // API
    boolean signInUser(String email, String password) throws IOException, JSONException, InterruptedException, ExecutionException, TimeoutException {
        // Chamada API para Login
        String addToUrl = "user/login?email=%22" + email + "%22&password=%22" + password + "%22";
        APICall a = new APICall(addToUrl);

        JSONObject res = a.getJson();
        String success = res.get("success").toString();
        if(success.equals("true")){
            UserInformation.email = email;
            UserInformation.token = res.get("token").toString();
            return true;
        }
        else{
            return false;
        }

    }

    public void msgBox_okbuttononly(String str, String str2)
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

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Tem a certeza que pretende sair?");
        builder.setCancelable(false);
        builder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        closeApplication(); // Close Application method called
                    }
                })
                .setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.dismiss();
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void closeApplication() {
        this.finish();
    }
}
