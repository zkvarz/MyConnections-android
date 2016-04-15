package com.example.myconnections_android.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.myconnections_android.R;
import com.example.myconnections_android.api.models.GcmToken;
import com.example.myconnections_android.api.requests.GcmRegistrationRequest;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.main.CoreApplication;
import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.preferences.AppPreference;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by kvarivoda on 05.04.2016.
 */
public class GcmIntentService extends IntentService {

    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(TAG);
    }

    public static final String KEY = "key";
    public static final String TOPIC = "topic";
    public static final String SUBSCRIBE = "subscribe";
    public static final String UNSUBSCRIBE = "unsubscribe";


    @Override
    protected void onHandleIntent(Intent intent) {
        String key = intent.getStringExtra(KEY);
        switch (key) {
            case SUBSCRIBE:
                // subscribe to a topic
                String topic = intent.getStringExtra(TOPIC);
                subscribeToTopic(topic);
                break;
            case UNSUBSCRIBE:
                break;
            default:
                // if key is specified, register with GCM
                registerGCM();
        }

    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private void registerGCM() {
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.e(TAG, "GCM Registration Token: " + token);

            // sending the registration id to our server
            sendRegistrationToServer(token);

            AppPreference.getInstance().setSentToken(true);
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
            AppPreference.getInstance().setSentToken(false);
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String gcmToken) {

        // checking for valid login session
        LoginResponse loginResponse = AppPreference.getInstance().getLoginResponse();
        if (loginResponse == null) {
            Logger.debug(getClass(), "user not found!");
            // TODO
            // user not found, redirecting him to login screen
            return;
        }

        new GcmRegistrationRequest(new GcmToken(gcmToken, loginResponse.getToken()), new ICallback<String>() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), "TOKEN SENT!", Toast.LENGTH_LONG).show();
                Logger.debug(getClass(), "TOKEN SENT!");

                Intent registrationComplete = new Intent(Config.SENT_TOKEN_TO_SERVER);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(registrationComplete);
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "updateProfile ERROR " + error.getErrorMessage());
                Toast.makeText(getApplicationContext(), "Unable to send gcm registration id to our sever. " /*+ obj.getJSONObject("error").getString("message")*/, Toast.LENGTH_LONG).show();
            }
        }).execute();

    }

    /**
     * Subscribe to a topic
     */
    public static void subscribeToTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(CoreApplication.getApplication().getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(CoreApplication.getApplication().getApplicationContext());
        String token;
        try {
            token = instanceID.getToken(CoreApplication.getApplication().getApplicationContext().getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.subscribe(token, "/topics/" + topic, null);
                Log.e(TAG, "Subscribed to topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(CoreApplication.getApplication().getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void unsubscribeFromTopic(String topic) {
        GcmPubSub pubSub = GcmPubSub.getInstance(getApplicationContext());
        InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
        String token;
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            if (token != null) {
                pubSub.unsubscribe(token, "");
                Log.e(TAG, "Unsubscribed from topic: " + topic);
            } else {
                Log.e(TAG, "error: gcm registration id is null");
            }
        } catch (IOException e) {
            Log.e(TAG, "Topic unsubscribe error. Topic: " + topic + ", error: " + e.getMessage());
            Toast.makeText(getApplicationContext(), "Topic subscribe error. Topic: " + topic + ", error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}