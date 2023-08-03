package com.example.jujojazbase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import json.JSONArray;
import json.JSONObject;
import kotlin.Unit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class Auth extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private Button signIn;
    private Intent intent;
    public static ModelUser user;
    public static List<ModelData> datas = new ArrayList<>();
    public static JSONObject authJson;
    private Boolean apiOnly = true;

    public void createFileForLogin(String userJson){
        File file = new File(getApplicationContext().getFilesDir(), "Auth");
        if (!file.exists()) {
            file.mkdir();
        }

        try {
            File gpxfile = new File(file, "Login");
            FileWriter writer = new FileWriter(gpxfile);
            System.out.println("UserJson : " + userJson);
            writer.append(userJson);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loginApiFail(JSONObject data){

    }

    public void loginApiSucceed(JSONObject data){

    }

    public void loginApiError(String msg){

    }

    public void loginSucceed(JSONObject data){

        intent = new Intent(getApplicationContext(), HomeActivity.class);
        createFileForLogin(authJson.toString());
        Log.d("Auth", data.toString());
        datas = JujojazLib.Companion.convertJSONToModelData(data);
        startActivity(intent);

        Intent broadcastNotification = new Intent(this, BroadcastNotification.class);
        sendBroadcast(broadcastNotification);

        Intent alarmManagerReceiver = new Intent(this, AlarmManagerReceiver.class);
        sendBroadcast(alarmManagerReceiver);

        finish();
    }
    public void showLoginUI(){
        setContentView(R.layout.activity_auth);

        signIn = findViewById(R.id.btnSignIn);
        signIn.setOnClickListener(this);

        email = findViewById(R.id.emailEText);
        password = findViewById(R.id.passEText);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("Password : Tekan Enter");
                    signIn.performClick();
                    return true;
                }
                return false;
            }
        });

    }
    public void loginFail(JSONObject data){
        showLoginUI();

        if (!isFinishing()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Auth.this);
            builder.setCancelable(true);
            builder.setInverseBackgroundForced(true);
            builder.setMessage("Anda Salah Memasukkan Username atau Password");
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    public Unit loginError(String msg){
        //set constant error message
        msg = "Koneksi error. harap periksa koneksi internet anda atau coba lagi nanti";

        if (!isFinishing()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setInverseBackgroundForced(true);
//            Log.println(Log.INFO, "loginError(): ", msg);
            builder.setMessage(msg);
            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    System.exit(0);
                }
            });

            builder.show();
        }


        return Unit.INSTANCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //store to temp, in case if any class need a non-null instance
        Temp.Companion.setAnyActivity(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        2);
            }
        }

        try {
            File file = new File(getApplicationContext().getFilesDir(), "Auth");
            File gpxfile = new File(file, "Login");
            FileReader reader = new FileReader(gpxfile);
            StringBuilder stringBuilder = new StringBuilder();
            int i;
            while ((i=reader.read()) != -1) {
                stringBuilder.append((char)i);
            }
            reader.close();
            System.out.println("LoginJson : " + stringBuilder.toString());
            JSONObject loginJson = new JSONObject(stringBuilder.toString());
            loginApi(loginJson.get("username").toString(), loginJson.get("password").toString(), false);
        } catch (Exception e) {
            System.out.println("Belum Login");
            showLoginUI();
        }
    }

    Unit loginResult(JSONObject data){
        Log.println(Log.INFO, "INFO", "|"+data.get("success").toString()+"|");
        if (data.get("success").toString().compareTo("1")==0) {
            loginApiSucceed(data);
            if (!apiOnly){
                loginSucceed(data);
            }
        } else {
            System.out.println("Fail");
            loginApiFail(data);
            if (!apiOnly){
                loginFail(data);
            }
        }
        return Unit.INSTANCE;
    }

    public JSONObject loginApi(String username, String password, final Boolean apiOnly){
        this.apiOnly = apiOnly;

        user = new ModelUser();
        user.setUsername(username);
        user.setPassword(password);

        authJson = new JSONObject();
        authJson.put("username", username);
        authJson.put("password", password);
        Log.d("Auth", authJson.toString()) ;
        JujojazLib.Companion.hitAPI("/api/allvehicles/", authJson, this, this::loginResult, this::loginError, true);
        try{

        }catch (RuntimeException e){
            String msg = e.getMessage();
            loginApiError(msg);
            if (!apiOnly){
                loginError(msg);
            }
        }

        return authJson;
    }


    public JSONObject loginApi(String username, String password){
        return loginApi(username, password, true);
    }

    @Override
    public void onClick(View v) {
        String username = ((EditText)findViewById(R.id.emailEText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passEText)).getText().toString();
        loginApi(username, password, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
