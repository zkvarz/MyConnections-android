package com.example.myconnections_android.api.responses;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by kvarivoda on 22.03.2016.
 */
public class LoginResponse implements Parcelable {
    private String id;
    private String phone;
    private String social;
    private String facebookId;
    private String facebookToken;
    private String token;
    private String expires;

    public LoginResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFacebookToken() {
        return facebookToken;
    }

    public void setFacebookToken(String facebookToken) {
        this.facebookToken = facebookToken;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public static final Creator<LoginResponse> CREATOR = new Creator<LoginResponse>() {
        @Override
        public LoginResponse createFromParcel(Parcel in) {
            return new LoginResponse(in);
        }

        @Override
        public LoginResponse[] newArray(int size) {
            return new LoginResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(phone);
        parcel.writeString(social);
        parcel.writeString(facebookId);
        parcel.writeString(facebookToken);
        parcel.writeString(token);
        parcel.writeString(expires);
    }

    private LoginResponse(Parcel in) {
        Log.d("parcel", "read from parcel");
        id = in.readString();
        phone = in.readString();
        social = in.readString();
        facebookId = in.readString();
        facebookToken = in.readString();
        token = in.readString();
        expires = in.readString();

    }
}
