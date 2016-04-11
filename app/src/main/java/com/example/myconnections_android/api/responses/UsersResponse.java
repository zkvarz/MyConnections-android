package com.example.myconnections_android.api.responses;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public class UsersResponse {
    //todo _id
    private String _id;
    private String phone;
    private String social;
    private String gcm_registration_id;

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

    public String getSocial() {
        return social;
    }

    public void setSocial(String social) {
        this.social = social;
    }

    public String getGcm_registration_id() {
        return gcm_registration_id;
    }

    public void setGcm_registration_id(String gcm_registration_id) {
        this.gcm_registration_id = gcm_registration_id;
    }
}
