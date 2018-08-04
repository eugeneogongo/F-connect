package com.youstar.f_connect.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.youstar.f_connect.Model.Product;
import com.youstar.f_connect.R;
import com.youstar.f_connect.Storage.Prefs;
import com.youstar.f_connect.UI.Home;
import com.youstar.f_connect.UI.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 */
public class Sale extends Fragment implements View.OnClickListener {


    private View view;
    private TextInputEditText productName;
    private TextInputEditText productDesc;
    private TextInputEditText productPrice;
    private Button btnPostproduct;
    private Button btnDiscard;
    private TextInputLayout layoutname;
    private TextInputLayout layoutproductname;
    private TextInputLayout layoutprice;
    private TextInputEditText productquantity;
    private TextInputLayout layoutquantity;
    HashMap<String,String> product;

    public Sale() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sale, container, false);


        initView(view);
        return view;
    }

    private void initView(View view) {
        productName = (TextInputEditText) view.findViewById(R.id.productName);
        productDesc = (TextInputEditText) view.findViewById(R.id.productDesc);
        productPrice = (TextInputEditText) view.findViewById(R.id.productPrice);
        btnPostproduct = (Button) view.findViewById(R.id.btnPostproduct);
        btnDiscard = (Button) view.findViewById(R.id.btnDiscard);

        btnPostproduct.setOnClickListener(this);
        btnDiscard.setOnClickListener(this);
        layoutname = (TextInputLayout) view.findViewById(R.id.layoutname);

        layoutproductname = (TextInputLayout) view.findViewById(R.id.layoutproductname);

        layoutprice = (TextInputLayout) view.findViewById(R.id.layoutprice);

        productquantity = (TextInputEditText) view.findViewById(R.id.productquantity);
        productquantity.setOnClickListener(this);
        layoutquantity = (TextInputLayout) view.findViewById(R.id.layoutquantity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPostproduct:
                submit();
                break;
            case R.id.btnDiscard:

                break;
        }
    }

    private void submit() {
        // validate
        String productNameString = productName.getText().toString().trim();
        if (TextUtils.isEmpty(productNameString)) {
layoutname.setError("Product Name cannot be Empty");
            return;
        }

        String productDescString = productDesc.getText().toString().trim();
        if (TextUtils.isEmpty(productDescString)) {
layoutproductname.setError("Write a short Description");
            return;
        }

        String productPriceString = productPrice.getText().toString().trim();
        if (TextUtils.isEmpty(productPriceString)) {
    layoutprice.setError("Price cannot be empty!");
            return;
        }
        String productquantityString = productquantity.getText().toString().trim();
        if (TextUtils.isEmpty(productquantityString)) {
           layoutquantity.setError("Quantity Available is required");
            return;
        }
     FirebaseDatabase storage = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = storage.getReference().child("posts");
        String postedon = new Date().toString();
        Product product = new Product(productNameString,productPriceString, FirebaseAuth.getInstance().getCurrentUser().getUid(),postedon,productquantityString,productDescString);
        databaseReference.push().setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                productName.setText("");
                productDesc.setText("");
                productPrice.setText("");
                productquantity.setText("");
                Toast.makeText(getContext(),"Product was successful added",Toast.LENGTH_LONG);
            }
        });








    }




}
