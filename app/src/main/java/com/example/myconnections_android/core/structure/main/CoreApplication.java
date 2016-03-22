package com.example.myconnections_android.core.structure.main;

import android.app.Application;


public class CoreApplication extends Application {

    private static CoreApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }


    public static CoreApplication getApplication() {
        return sApplication;
    }

}
