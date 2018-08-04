package com.youstar.f_connect.Storage;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by odera on 3/5/2018.
 */

public class Prefs {
    Context context;
    SharedPreferences prefs;
    String prefname="id";
    String username="username";
    SharedPreferences.Editor editor;

    public Prefs(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(prefname,0);
        editor = prefs.edit();
    }
    public void setID(int id){
        editor.putInt(prefname,id);
        editor.commit();
    }
    public int getId(){
        return prefs.getInt(prefname,0);
    }
    public void setUsername(String name){
        editor.putString(username,name);
        editor.commit();
    }
    public String getUsername(){
        return prefs.getString(username,"name");
    }
}
