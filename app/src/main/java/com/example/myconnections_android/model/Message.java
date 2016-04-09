package com.example.myconnections_android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myconnections_android.api.responses.LoginResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kvarivoda on 05.04.2016.
 */
public class Message implements Parcelable {
    private String id, message;
    private String timestamp;

    @SerializedName("chat_room_id")
    private String chatRoomId;

    private LoginResponse loginResponse;

    public Message() {

    }

    public Message(String id, String message, String timestamp, String chatRoomId, LoginResponse loginResponse) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.chatRoomId = chatRoomId;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public LoginResponse getLoginResponse() {
        return loginResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Message(Parcel in) {
        loginResponse = (LoginResponse) in.readParcelable(LoginResponse.class.getClassLoader());
        id = in.readString();
        message = in.readString();
        timestamp = in.readString();
        chatRoomId = in.readString();

      /*  String[] data = new String[3];

        in.readStringArray(data);
        this.id = data[0];
        this.message = data[1];
        this.timestamp = data[2];
        this.chatRoomId = data[3];*/
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(loginResponse, flags);
        parcel.writeString(id);
        parcel.writeString(message);
        parcel.writeString(timestamp);
        parcel.writeString(chatRoomId);


      /*  parcel.writeStringArray(new String[]{this.id,
                this.message,
                this.timestamp});*/
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
