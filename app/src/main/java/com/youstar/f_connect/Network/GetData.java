package com.youstar.f_connect.Network;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.youstar.f_connect.Model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by odera on 3/6/2018.
 */

public class GetData extends AsyncTask<Void,Void,String> {

    Context context;

    public List<Product> getProductList() {
        return productList;
    }

    List<Product> productList;
    private HttpURLConnection conn;


    public GetData(Context context) {
        this.context = context;
        productList = new ArrayList<>();
    }

    private ProgressDialog dialog;

    @Override
    protected String doInBackground(Void... voids) {
        String response="";
        String urlstring="http://192.168.0.141/farm/GetData.php";

        URL url = null;
        try {
            url = new URL(urlstring);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setReadTimeout( 10000 /*milliseconds*/ );
        conn.setConnectTimeout( 15000 /* milliseconds */ );
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        String line;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while ((line = br.readLine()) != null) {
                response += line;
                Log.d("output lines", line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.trim();
}



    @Override
    protected void onPostExecute(String s) {
        try {
        Log.i("response",s);
        JSONObject object = new JSONObject(s);
            JSONArray jsonarray = object.getJSONArray("data");
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String productname = jsonobject.getString("productname");
                String des = jsonobject.getString("des");
                String price = jsonobject.getString("price");
                String quantity =jsonobject.getString("quantity");
                String postedby = jsonobject.getString("name");
                String dateposted = jsonobject.getString("dateposted");

                productList.add(new Product(productname,price,postedby,dateposted,quantity,des));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}

