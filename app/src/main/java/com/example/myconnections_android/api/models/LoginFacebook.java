package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 22.03.2016.
 */
public class LoginFacebook {
    private String id;
    private String facebookToken;

    public LoginFacebook(String id, String facebookToken) {
        this.id = id;
        this.facebookToken = facebookToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }
}
