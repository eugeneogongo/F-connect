package com.eugene.expenditure;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Home extends AppCompatActivity {
    Resources res;
    String [] months;



    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        FrameLayout main = (FrameLayout) findViewById(R.id.framelayout);
        months = res.getStringArray(R.array.months);

        int numviews = months.length;
        for(int i=0;i<numviews;i++)
        {
            TextView txtmonth =  new TextView(this);
            txtmonth.setText(months[i]);
            txtmonth.setGravity(Gravity.CENTER);


            FrameLayout.LayoutParams ip=new FrameLayout.LayoutParams(150,100);
            ip.gravity=Gravity.CENTER;
            txtmonth.setLayoutParams(ip);
            float angleDeg = i * 360.0f/numviews-90.0f;
            float angleRad = (float)(angleDeg*Math.PI/180.0f);
            txtmonth.setTranslationX(300*(float)Math.cos(angleRad));
            txtmonth.setTranslationY(300*(float)Math.sin(angleRad));
            txtmonth.setRotation(angleDeg+90.0f);
            main.addView(txtmonth);
        }

    }
}
