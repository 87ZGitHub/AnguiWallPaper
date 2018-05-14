package com.greatwall.papervolly.application;

import android.app.Application;

import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication {
    public static Application application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
