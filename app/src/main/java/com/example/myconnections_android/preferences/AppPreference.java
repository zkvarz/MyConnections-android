package com.example.myconnections_android.preferences;

import android.content.SharedPreferences;

import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.main.CoreApplication;
import com.google.gson.Gson;

import static android.text.TextUtils.isEmpty;

/**
 * Created by kvarivoda on 05.04.2016.
 */
public class AppPreference {
    public static final String SHARED_PREF = "sharedPref";
    public static final String LOGIN_RESPONSE = "LOGIN_RESPONSE";
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    private static final String KEY_NOTIFICATIONS = "notifications";

    private static AppPreference appPreference;
    private static SharedPreferences sharedPreference;

    public static synchronized AppPreference getInstance() {
        if (appPreference == null) {
            appPreference = new AppPreference();
        }
        return appPreference;
    }

    public SharedPreferences getSharedPreference() {
        if (sharedPreference == null) {
            sharedPreference = CoreApplication.getApplication().getSharedPreferences(SHARED_PREF, 0);
        }
        return sharedPreference;
    }

    public void clearPreferences() {
        SharedPreferences.Editor prefsEditor = getSharedPreference().edit();
        prefsEditor.clear();
        prefsEditor.apply();
    }

    public void setLoginResponse(String loginResponse) {
        SharedPreferences.Editor prefsEditor = getSharedPreference().edit();
        prefsEditor.putString(LOGIN_RESPONSE, loginResponse);
        prefsEditor.apply();
    }

    public LoginResponse getLoginResponse() {
        String json = getSharedPreference().getString(LOGIN_RESPONSE, null);
        if (!isEmpty(json)) {
            return new Gson().fromJson(json, LoginResponse.class);
        }
        return null;
    }

    public void setSentToken(boolean isSentToken) {
        SharedPreferences.Editor prefsEditor = getSharedPreference().edit();
        prefsEditor.putBoolean(SENT_TOKEN_TO_SERVER, isSentToken);
        prefsEditor.apply();
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        //TODO: can be shortened to "edit"
        getSharedPreference().edit().putString(KEY_NOTIFICATIONS, oldNotifications);
        getSharedPreference().edit().commit();
    }

    public String getNotifications() {
        return sharedPreference.getString(KEY_NOTIFICATIONS, null);
    }

    public boolean isSessionValid() {
        long currentTimeSeconds = System.currentTimeMillis() / 1000;
        if (getLoginResponse() != null) {
            Logger.debug(getClass(), "currentTime " + currentTimeSeconds);
            Logger.debug(getClass(), "getExpires " + getLoginResponse().getExpires());
            return currentTimeSeconds < Long.valueOf(getLoginResponse().getExpires());
        }
        return false;
    }
}
