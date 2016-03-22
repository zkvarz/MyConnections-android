package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 22.03.2016.
 */
public class LoginFacebook {
    private String id;
    private String token;

    public LoginFacebook(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
