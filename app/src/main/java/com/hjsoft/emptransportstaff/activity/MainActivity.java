package com.hjsoft.emptransportstaff.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.hjsoft.emptransportstaff.R;
import com.hjsoft.emptransportstaff.SessionManager;
import com.hjsoft.emptransportstaff.model.Pojo;
import com.hjsoft.emptransportstaff.webservices.API;
import com.hjsoft.emptransportstaff.webservices.RestClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjsoft on 7/6/17.
 */
public class MainActivity extends AppCompatActivity {

    EditText etUname,etPwd;
    Button btLogin;
    String stUname,stPwd;
    API REST_CLIENT;
    SessionManager session;
    HashMap<String, String> user;
    String uname,pwd;
    ProgressDialog progressDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "sp";
    String version="12",companyId="CMP0001";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUname=(EditText)findViewById(R.id.am_et_uname);
        etPwd=(EditText)findViewById(R.id.am_et_pwd);
        btLogin=(Button)findViewById(R.id.am_bt_login);
        session=new SessionManager(MainActivity.this);

        //HJ Swathi 9032871453 .. HJ Santosh 7732035678

        pref = getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        REST_CLIENT= RestClient.get();

        user = session.getUserDetails();

        uname=user.get(SessionManager.KEY_NAME);
        pwd=user.get(SessionManager.KEY_PWD);

        if(session.isLoggedIn())
        {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();


            JsonObject v=new JsonObject();
            v.addProperty("login",uname);
            v.addProperty("pwd",pwd);
            v.addProperty("companycode",companyId);
            v.addProperty("version",version);

            Call<Pojo> call=REST_CLIENT.userLogin(v);
            call.enqueue(new Callback<Pojo>() {
                @Override
                public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                    progressDialog.dismiss();

                    if(response.isSuccessful())
                    {

                        session.createLoginSession(uname,pwd);

                        // Toast.makeText(MainActivity.this,"Valid details!",Toast.LENGTH_LONG).show();
                        Intent i=new Intent(MainActivity.this,TripSummaryActivity.class);
                        startActivity(i);
                        finish();

                    }
                    else {

                        String msg=response.message();

                        if(msg.equals("Old Version")) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.alert_version, null);
                            dialogBuilder.setView(dialogView);

                            Button ok = (Button) dialogView.findViewById(R.id.av_bt_ok);

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();
                            alertDialog.setCanceledOnTouchOutside(false);
                            alertDialog.setCancelable(false);

                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    alertDialog.dismiss();
                                    finish();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onFailure(Call<Pojo> call, Throwable t) {

                    progressDialog.dismiss();

                    Toast.makeText(MainActivity.this,"Check Internet Connection!",Toast.LENGTH_LONG).show();

                    finish();

                }
            });

        }

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //balreddys 123456789012

                stUname=etUname.getText().toString().trim();
                stPwd=etPwd.getText().toString().trim();

                if(stUname.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter Username!",Toast.LENGTH_SHORT).show();

                }
                else if(stPwd.equals(""))
                {
                    Toast.makeText(MainActivity.this,"Enter Password",Toast.LENGTH_LONG).show();
                }
                else {

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Please wait...");
                    progressDialog.show();

                    JsonObject v=new JsonObject();
                    v.addProperty("login",stUname);
                    v.addProperty("pwd",stPwd);
                    v.addProperty("companycode",companyId);
                    v.addProperty("version",version);

                    Call<Pojo> call=REST_CLIENT.userLogin(v);
                    call.enqueue(new Callback<Pojo>() {
                        @Override
                        public void onResponse(Call<Pojo> call, Response<Pojo> response) {

                            progressDialog.dismiss();


                            Pojo data;

                            if(response.isSuccessful())
                            {
                                session.createLoginSession(stUname,stPwd);
                                data=response.body();
                                editor.putString("empId",data.getMessage());
                                editor.commit();

                                // Toast.makeText(MainActivity.this,"Valid details!",Toast.LENGTH_LONG).show();
                                Intent i=new Intent(MainActivity.this,TripSummaryActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else {

                                String msg=response.message();

                                if(msg.equals("Old Version"))
                                {
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);

                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.alert_version, null);
                                    dialogBuilder.setView(dialogView);

                                    Button ok = (Button) dialogView.findViewById(R.id.av_bt_ok);

                                    final AlertDialog alertDialog = dialogBuilder.create();
                                    alertDialog.show();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.setCancelable(false);

                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            alertDialog.dismiss();
                                            finish();
                                        }
                                    });
                                }
                                else {

                                    Toast.makeText(MainActivity.this,"Invalid details!",Toast.LENGTH_SHORT).show();
                                    etUname.setText("");
                                    etPwd.setText("");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Pojo> call, Throwable t) {

                            progressDialog.dismiss();

                            Toast.makeText(MainActivity.this,"Check Internet Connection!",Toast.LENGTH_LONG).show();

                            finish();

                        }
                    });
                }


            }
        });


    }
}
