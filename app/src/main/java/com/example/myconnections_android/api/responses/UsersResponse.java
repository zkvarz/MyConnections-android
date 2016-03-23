package com.example.myconnections_android.api.responses;

import java.util.ArrayList;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public class UsersResponse {
    private String _id;
    private String phone;
    private String password;

    private ArrayList userArray;

    public ArrayList getUserArray() {
        return userArray;
    }

    public void setUserArray(ArrayList userArray) {
        this.userArray = userArray;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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
