package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public class Session {
    private String token;

    public Session(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
