package com.youstar.f_connect.Fragments;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.youstar.f_connect.Model.Product;
import com.youstar.f_connect.Network.Recently;
import com.youstar.f_connect.R;
import com.youstar.f_connect.Storage.Prefs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment {

    HashMap<String, String> product;
    private View view;
    private TextView clientname;
    private TextView phonenumber;
    private RecyclerView prodcutsnearyourecyclerview;
    private HorizontalAdapter adapter2;
    private SwipeRefreshLayout swipetorefresh;
    FirebaseDatabase mref;
    DatabaseReference databaseReference;
    ChildEventListener listener;



    public Account() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();

    }



    private void initView() {
        clientname = (TextView) view.findViewById(R.id.clientname);
        phonenumber = (TextView) view.findViewById(R.id.phonenumber);
        prodcutsnearyourecyclerview = (RecyclerView) view.findViewById(R.id.prodcutsnearyourecyclerview);
        prodcutsnearyourecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        prodcutsnearyourecyclerview.setItemAnimator(new DefaultItemAnimator());
        prodcutsnearyourecyclerview.setHasFixedSize(true);
        Recently rec = new Recently(getContext(), new Prefs(getContext()).getId());
        adapter2 = new HorizontalAdapter();
        swipetorefresh = view.findViewById(R.id.swipetorefresh);
        mref = FirebaseDatabase.getInstance();
        databaseReference = mref.getReference().child("posts");
        listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                adapter2.addProduct(product);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        prodcutsnearyourecyclerview.setAdapter(adapter2);

        swipetorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter2.notifyDataSetChanged();
                swipetorefresh.setRefreshing(false);


            }
        });
    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {
        List<Product> productList;


        public HorizontalAdapter() {
            productList = new ArrayList<>();
        }
        public  void addProduct(Product product){
            productList.add(product);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.horizontal_item_view, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

            Product product = productList.get(position);
            holder.itemimage.setImageResource(R.mipmap.ic_account);
            holder.name.setText(product.getProductname());
            holder.price.setText("Ksh " + product.getPrice());


        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView itemimage;
            TextView name, price;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemimage = itemView.findViewById(R.id.itemimage);
                name = itemView.findViewById(R.id.itemname);
                price = itemView.findViewById(R.id.itemprice);
            }
        }
    }
}




