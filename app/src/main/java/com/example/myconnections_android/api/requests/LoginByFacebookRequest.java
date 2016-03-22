package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.LoginFacebook;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.client.PostRequest;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;

import static com.example.myconnections_android.api.Api.getLoginByFacebookUrl;


/**
 * Created by kvarivoda on 22.03.2016.
 */
public class LoginByFacebookRequest extends PostRequest<LoginResponse> {

    private final LoginFacebook loginFacebook;

    public LoginByFacebookRequest(LoginFacebook loginFacebook, ICallback<LoginResponse> callback) {
        super(callback);
        this.loginFacebook = loginFacebook;
    }

    @Override
    protected void parseResponse(RemoteResponse response) {
        String responseString = response.toString();
        try {
            if (response.isSuccess()) {
                try {
                    LoginResponse fbLoginResponse = new Gson().fromJson(responseString,
                            LoginResponse.class);
                    Logger.debug(getClass(), "fb: " + fbLoginResponse.get_id());
                    Logger.debug(getClass(), "fb getFacebookId: " + fbLoginResponse.getFacebookId());
                    Logger.debug(getClass(), "fb getSocial: " + fbLoginResponse.getSocial());
                    Logger.debug(getClass(), "JSON : " + new Gson().toJson(LoginResponse.class));
                    onSuccess(fbLoginResponse);
                } catch (Exception e) {
                    onError(e);
                }
            } else {
                Logger.error(getClass(), "Error while getting facebook login response");
                Logger.error(getClass(), "responseString "   + responseString);
                ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
                onError(apiError.getError());
            }
        } catch (Exception e) {
            e.printStackTrace();
            onError(e);
        }
    }

    @Override
    protected String buildUrl() {
        return getLoginByFacebookUrl();
    }

    @Override
    protected HttpEntity buildEntity() {
        try {
            return new StringEntity(new Gson().toJson(loginFacebook));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
