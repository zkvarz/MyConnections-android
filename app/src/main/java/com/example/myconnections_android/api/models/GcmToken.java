package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 05.04.2016.
 */
public class GcmToken {
    private String gcmToken;
    private String token;

    public GcmToken(String gcmToken, String token) {
        this.gcmToken = gcmToken;
        this.token = token;
    }

    public String getGcmToken() {
        return gcmToken;
    }

    public void setGcmToken(String gcmToken) {
        this.gcmToken = gcmToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
