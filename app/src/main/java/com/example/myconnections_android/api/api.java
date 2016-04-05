package com.example.myconnections_android.api;

import android.net.Uri;

/**
 * Builds urls for all kind of requests
 */
public class Api {
    private static final String SCHEMA = "https";
    private static final String DEV_SERVER = "myconnections-backend-zkvarz.c9users.io";
    private static final String ACCOUNT = "account";
    private static final String CREATE_USER = "createUser";
    private static final String LOGIN_BY_FACEBOOK = "facebookLogin";
    private static final String LOGIN_BY_TWITTER = "twitterLogin";
    private static final String LOGIN_BY_GOOGLE = "googleLogin";
    private static final String GET_USERS = "getUsers";
    private static final String UPDATE_USER = "updateUser";

    private static Uri.Builder getAccountUriBuilder() {
        return getRootUriBuilder().appendPath(ACCOUNT);
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

}
