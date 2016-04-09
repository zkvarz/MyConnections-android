package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 09.04.2016.
 */
public class ObjectId {
    private String token;
    private String id;

    public ObjectId(String token, String id) {
        this.token = token;
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
