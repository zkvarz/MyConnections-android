package com.example.myconnections_android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myconnections_android.api.responses.LoginResponse;

/**
 * Created by kvarivoda on 05.04.2016.
 */
public class Message implements Parcelable {
    private String id, message, createdAt;
    private LoginResponse loginResponse;

    public Message() {

    }

    public Message(String id, String message, String createdAt, LoginResponse loginResponse) {
        this.id = id;
        this.message = message;
        this.createdAt = createdAt;
        this.loginResponse = loginResponse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public Message(Parcel in) {
        String[] data = new String[3];

        in.readStringArray(data);
        this.id = data[0];
        this.message = data[1];
        this.createdAt = data[2];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.id,
                this.message,
                this.createdAt});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}
