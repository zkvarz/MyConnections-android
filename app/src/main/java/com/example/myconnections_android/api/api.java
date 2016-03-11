package com.example.myconnections_android.api;

import android.net.Uri;

/**
 * Builds urls for all kind of requests
 */
public class Api {
    private static final String SCHEMA = "http";
    private static final String DEV_SERVER = "ide.c9.io/zkvarz/myconnections-backend";
    private static final String ACCOUNT = "account";
    private static final String CREATE_USER = "createUser";

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

}
