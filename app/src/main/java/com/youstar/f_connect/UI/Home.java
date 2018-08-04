package com.youstar.f_connect.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.youstar.f_connect.Fragments.Account;
import com.youstar.f_connect.Fragments.Products;
import com.youstar.f_connect.Fragments.Sale;
import com.youstar.f_connect.R;
import com.youstar.f_connect.Storage.Prefs;

import java.util.Arrays;
import java.util.List;

public class Home extends AppCompatActivity {

    private TextView txttoolbar;
    private Toolbar toolbar;
    private ViewPager viewpager;
    private BottomNavigationView bottomnav;
    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IsLogin();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void IsLogin() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
                if(user==null){

// Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.PhoneBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build());
// Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.shop)
                                    .setIsSmartLockEnabled(false)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        });
        t.start();
    }


    private void initView() {
        txttoolbar = (TextView) findViewById(R.id.txttoolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txttoolbar.setText("Farm Products for Sale");
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager.setAdapter(new viewpageradapter(getSupportFragmentManager()));

        bottomnav = (BottomNavigationView) findViewById(R.id.bottomnav);
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuproducts:
                        viewpager.setCurrentItem(0);
                        txttoolbar.setText("Farm Products for Sale");
                        break;
                    case R.id.menupostproduct:
                        viewpager.setCurrentItem(1);
                        txttoolbar.setText("Post a product");
                        break ;
                    case R.id.menuprofile:
                        viewpager.setCurrentItem(2);
                        txttoolbar.setText("Your Profile");
                }
                return  true;
            }
        });
    }

    class viewpageradapter extends FragmentPagerAdapter{

        public viewpageradapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
           switch (position){
               case 0: return new Products();
               case 1: return  new Sale();
               case 2: return new Account();
               default: return  new Products();
           }
        }


        @Override
        public int getCount() {
            return 3;
        }
    }


}
