package com.youstar.f_connect.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.youstar.f_connect.Model.Product;
import com.youstar.f_connect.Network.GetData;
import com.youstar.f_connect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Products extends Fragment {


    private View view;
    private RecyclerView recyclerviewproductsnearyou;
    private RecyclerView prodcutsnearyourecyclerview;
    private SwipeRefreshLayout swipetorefresh;
    private ProgressBar progressBar;
    ChildEventListener listener;
    FirebaseDatabase mref;
    DatabaseReference databaseReference;

    private HorizontalAdapter adapter2;

    public Products() {
        // Required empty public constructor
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_products, container, false);


        if (!isNetworkAvailable()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.alert_message)
                    .setTitle(R.string.alert_title)
                    .setCancelable(false)
                    .setPositiveButton(R.string.alert_positive,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    getActivity().finish();
                                }
                            });

            AlertDialog alert = builder.create();
            alert.show();

        }
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mref = FirebaseDatabase.getInstance();
        databaseReference = mref.getReference().child("posts");
        loadFeed();
        initView(view);
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
        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        databaseReference.orderByChild("postedon").addChildEventListener(listener);
    }

    @Override
    public void onPause() {
        super.onPause();
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        if (adapter2 != null) {
            for (int i = 0; i < adapter2.getItemCount(); i++) {
                adapter2.notifyItemRemoved(i);
            }
        }

    }

    private void initView(View view) {
        recyclerviewproductsnearyou = (RecyclerView) view.findViewById(R.id.recyclerviewproductsnearyou);
        prodcutsnearyourecyclerview = (RecyclerView) view.findViewById(R.id.prodcutsnearyourecyclerview);
        recyclerviewproductsnearyou.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.HORIZONTAL, false));
        recyclerviewproductsnearyou.setItemAnimator(new DefaultItemAnimator());
        recyclerviewproductsnearyou.setHasFixedSize(true);
        prodcutsnearyourecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        prodcutsnearyourecyclerview.setItemAnimator(new DefaultItemAnimator());
        prodcutsnearyourecyclerview.setHasFixedSize(true);
        swipetorefresh = view.findViewById(R.id.swipetorefresh);
        recyclerviewproductsnearyou.setAdapter(adapter2);
        prodcutsnearyourecyclerview.setAdapter(adapter2);
        swipetorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter2.notifyDataSetChanged();
                swipetorefresh.setRefreshing(false);
                loadFeed();
            }
        });


    }

    private void loadFeed() {
        adapter2 = new HorizontalAdapter();
        progressBar.setVisibility(View.GONE);
        adapter2.notifyDataSetChanged();



    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {
        List<Product> productList;

    public  void addProduct(Product product){
        productList.add(product);
    }

        public HorizontalAdapter() {
        productList = new ArrayList<>();
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
            holder.itemimage.setImageResource(R.mipmap.ova);
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
