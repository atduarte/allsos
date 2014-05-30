package com.example.AllSOSservice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class SettingsActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        final Button submitChanges = (Button) findViewById(R.id.btn_submitChanges);
        final Button back = (Button) findViewById(R.id.btn_back_s);

        final EditText newEmail = (EditText) findViewById(R.id.txt_newEmail);
        final EditText newNumber = (EditText) findViewById(R.id.txt_newTelNumber);
        final EditText newPassword = (EditText) findViewById(R.id.txt_newPassword);
        final EditText newConfirmPass = (EditText) findViewById(R.id.txt_confirmPassword);
        final EditText newLocation = (EditText) findViewById(R.id.txt_local);
        final EditText newRange = (EditText) findViewById(R.id.txt_alcance);

        submitChanges.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // chamada api pa adicionar
                String email = newEmail.getText().toString();
                String number = newNumber.getText().toString();
                String password = newPassword.getText().toString();
                String confirmPass = newConfirmPass.getText().toString();
                String location = newLocation.getText().toString();
                String range = newRange.getText().toString();

                try {
                    if(submitChanges(email,number,password,confirmPass,location,range)){
                        msgBox_okbuttononly("Sucesso", "Alterações efetuadas com sucesso");
                        Intent intent = new Intent(SettingsActivity.this, LoggedInActivity.class);
                        intent.
                        SettingsActivity.this.startActivity(intent);
                        finish();
                    }
                    else{
                        msgBox_okbuttononly("Erro", "Não foi possível efectuar as alterações. Por favor verifique as suas credenciais");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, LoggedInActivity.class);
                SettingsActivity.this.startActivity(intent);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, LoggedInActivity.class);
        SettingsActivity.this.startActivity(intent);
        finish();
    }


    boolean submitChanges(String email, String number, String pw, String conf_pw, String location, String range) throws InterruptedException, ExecutionException, TimeoutException, JSONException {
        if(!pw.equals(conf_pw)){
            return false;
        }
        else{
            // TODO: rever url
            String url = "changeInfo?email="+email+"&token="+UserInformation.token+"&password="+pw;
            APICall a = new APICall(url);
            JSONObject res = a.getJson();

            String success = res.get("success").toString();
            if(success.equals("true")){
                return true;
            }
            else{
                return false;
            }
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
}
