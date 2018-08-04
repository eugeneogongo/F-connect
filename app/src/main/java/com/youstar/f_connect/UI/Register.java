package com.youstar.f_connect.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.youstar.f_connect.R;
import com.youstar.f_connect.Storage.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText input_name;
    private EditText input_phonenumber;
    private AppCompatButton btn_signup;
    private TextView link_login;
    HashMap<String,String> Details =  new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        input_name = (EditText) findViewById(R.id.input_name);
        input_phonenumber = (EditText) findViewById(R.id.input_phonenumber);
        btn_signup = (AppCompatButton) findViewById(R.id.btn_signup);
        link_login = (TextView) findViewById(R.id.link_login);

        btn_signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                submit();

                break;
        }
    }

    private void submit() {
        // validate
        String name = input_name.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String phonenumber = input_phonenumber.getText().toString().trim();
        if (TextUtils.isEmpty(phonenumber)) {
            Toast.makeText(this, "Phone Number cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }


        Details.put("username",name);
        Details.put("phonenumber",phonenumber);
        String json =  new JSONObject(Details).toString();
        new RegisterTask().execute();
    }
    class RegisterTask extends AsyncTask<Void,Void,String>{
        String urlstring = "http://192.168.137.1/farm/registeruser.php";
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Register.this);
            dialog.setMessage("Regsitering");
            dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(Void... Void) {
            String response="";
            HttpURLConnection conn = null;
            try {
                URL url = new URL(urlstring);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout( 10000 /*milliseconds*/ );
                conn.setConnectTimeout( 15000 /* milliseconds */ );
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                String message = new JSONObject(Details).toString();
                conn.setFixedLengthStreamingMode(message.getBytes().length);

                //make some HTTP header nicety
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");

                //open
                conn.connect();

                //setup send
                BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(message.getBytes());
                //clean up
                os.flush();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    while ((line = br.readLine()) != null) {
                        response += line;
                        Log.d("output lines", line);
                    }
                } else {
                    response = "";
                }




            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
            Log.i("response",response);

            return response;
        }


        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            Log.i("id",s);
            JSONObject object = null;
            if(s==""){
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setTitle("An error Occured").setMessage("We couldnt Verify your Details");
                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }
            try {
                object = new JSONObject(s);
                System.out.println(object.toString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                int id = object.getInt("id");
                if(id!=0){
                    new Prefs(Register.this).setID(id);
                    new Prefs(Register.this).setUsername(input_name.getText().toString());
                    startActivity(new Intent(Register.this,Home.class));
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setTitle("An error Occured").setMessage("We couldnt Verify your Details");
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
