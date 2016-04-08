package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 08.04.2016.
 */
public class MessageSend {
    private String token;
    private String message;
    private String chatRoomId;

    public MessageSend(String token, String message, String chatRoomId) {
        this.token = token;
        this.message = message;
        this.chatRoomId = chatRoomId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
