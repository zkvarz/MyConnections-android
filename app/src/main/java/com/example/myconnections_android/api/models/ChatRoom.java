package com.example.myconnections_android.api.models;

/**
 * Created by kvarivoda on 09.04.2016.
 */
public class ChatRoom {
    private String token;
    private String chatRoomId;

    public ChatRoom(String token, String chatRoomId) {
        this.token = token;
        this.chatRoomId = chatRoomId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String id) {
        this.chatRoomId = id;
    }
}
