package com.youstar.f_connect.UI;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.youstar.f_connect.R;

public class Welcome extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSlideOverAnimation();
        addSlide(AppIntroFragment.newInstance("Lets Connect You with Buyers", "We connect you with prospective buyers", R.mipmap.ova, Color.parseColor("#3F51B5")));
        addSlide(AppIntroFragment.newInstance("There is a ready Market ", "Let us look for market for you ", R.drawable.shop, Color.parseColor("#3F51B5")));
    }
    @Override
        public void onSkipPressed(Fragment currentFragment){
            super.onSkipPressed(currentFragment);
            gotoHome();
        }
/*
* This method launches the Home activity*/
    private void gotoHome() {
        startActivity(new Intent(Welcome.this,Register.class));
    }

    @Override
        public void onDonePressed(Fragment currentFragment){
            super.onDonePressed(currentFragment);
            gotoHome();
        }
    }

