package com.example.myconnections_android.api;

import android.net.Uri;

/**
 * Builds urls for all kind of requests
 */
public class ApiUrl {
    private static final String SCHEMA = "https";
    private static final String DEV_SERVER = "myconnections-backend-zkvarz.c9users.io";
    private static final String ACCOUNT = "account";
    private static final String CREATE_USER = "createUser";
    private static final String LOGIN = "login";
    private static final String LOGIN_BY_FACEBOOK = "facebookLogin";
    private static final String LOGIN_BY_TWITTER = "twitterLogin";
    private static final String LOGIN_BY_GOOGLE = "googleLogin";
    private static final String GET_USERS = "getUsers";
    private static final String UPDATE_USER = "updateUser";
    private static final String USER = "user";
    private static final String GCM_CHAT = "chat";
    private static final String GET_CHAT_ROOMS = "getChatRooms";
    private static final String GET_PRIVATE_CHAT_ROOMS = "getChatPrivateRoom";
    private static final String GET_CHAT_ROOM_MESSAGES = "getChatRoomMessages";
    private static final String SEND_MESSAGE = "sendMessage";
    private static final String GCM_REGISTRATION = "gcmRegistration";

    private static Uri.Builder getAccountUriBuilder() {
        return getRootUriBuilder().appendPath(ACCOUNT);
    }

    private static Uri.Builder getChatUriBuilder() {
        return getRootUriBuilder().appendPath(GCM_CHAT);
    }

    public static Uri.Builder getRootUriBuilder() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEMA).encodedAuthority(DEV_SERVER);
        return builder;
    }

    public static String getCreateUserUrl() {
        return getAccountUriBuilder()
                .appendPath(CREATE_USER)
                .build()
                .toString();
    }

    public static String getLoginUrl() {
        return getAccountUriBuilder()
                .appendPath(LOGIN)
                .build()
                .toString();
    }


    public static String getLoginByFacebookUrl() {
        return getAccountUriBuilder()
                .appendPath(LOGIN_BY_FACEBOOK)
                .build()
                .toString();
    }

    public static String getLoginByTwitterUrl() {
        return getAccountUriBuilder()
                .appendPath(LOGIN_BY_TWITTER)
                .build()
                .toString();
    }

    public static String getLoginByGoogleUrl() {
        return getAccountUriBuilder()
                .appendPath(LOGIN_BY_GOOGLE)
                .build()
                .toString();
    }

    public static String getUsersUrl() {
        return getAccountUriBuilder()
                .appendPath(GET_USERS)
                .build()
                .toString();
    }

    public static String getUpdateUserUrl() {
        return getAccountUriBuilder()
                .appendPath(UPDATE_USER)
                .build()
                .toString();
    }

    public static String getGcmRegistrationUrl() {
        return getAccountUriBuilder()
                .appendPath(GCM_REGISTRATION)
                .build()
                .toString();
    }

    public static String getChatRoomsUrl() {
        return getChatUriBuilder()
                .appendPath(GET_CHAT_ROOMS)
                .build()
                .toString();
    }

    public static String getChatRoomMessagesUrl() {
        return getChatUriBuilder()
                .appendPath(GET_CHAT_ROOM_MESSAGES)
                .build()
                .toString();
    }

    public static String getSendMessageUrl() {
        return getChatUriBuilder()
                .appendPath(SEND_MESSAGE)
                .build()
                .toString();
    }

    public static String getChatPrivateRoomUrl() {
        return getChatUriBuilder()
                .appendPath(GET_PRIVATE_CHAT_ROOMS)
                .build()
                .toString();
    }


}
