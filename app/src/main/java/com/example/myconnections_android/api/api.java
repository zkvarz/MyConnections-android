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

}
