package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 15.04.2016.
 */
public class Login {
    private String phone;
    private String password;

    public Login(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
