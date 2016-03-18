package com.seven.actionbar;

import android.app.Application;

import java.util.HashMap;

/**
 * Created on 11/9/2015.
 */
public class MyApp extends Application {
    public HashMap<String, String> uMap;
    public HashMap<String, String> e_uMap;

    @Override
    public void onCreate(){
        super.onCreate();
        uMap = new HashMap<String, String>();
        e_uMap = new HashMap<String, String>();
    }
}
