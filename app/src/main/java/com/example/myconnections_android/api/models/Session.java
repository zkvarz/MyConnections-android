package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public class Session {
    private String token;
    private String secret;

    public Session(String token, String secret) {
        this.token = token;
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
