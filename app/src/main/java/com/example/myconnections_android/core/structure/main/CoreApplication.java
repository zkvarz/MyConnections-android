package com.example.myconnections_android.core.structure.main;

import android.app.Application;

import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;


public class CoreApplication extends Application {

    private static CoreApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        // Example: single kit
         TwitterAuthConfig authConfig =  new TwitterAuthConfig("3btcNQUEvlEIH6S4UPpFsDxqI", "dZr979eDF9eisvRhKLCtJE65Cumdxy5amE5KJX557aBJfbteOC");
         Fabric.with(this, new TwitterCore(authConfig));

        // Example: multiple kits
        // Fabric.with(this, new TwitterCore(authConfig), new TweetUi());

    }


    public static CoreApplication getApplication() {
        return sApplication;
    }

}
