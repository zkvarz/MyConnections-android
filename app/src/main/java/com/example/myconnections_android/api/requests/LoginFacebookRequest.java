package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.LoginFacebook;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.google.gson.Gson;

import static com.example.myconnections_android.api.ApiUrl.getLoginByFacebookUrl;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public class LoginFacebookRequest extends PostConnection<LoginResponse> {

    private LoginFacebook loginFacebook;

    public LoginFacebookRequest(LoginFacebook loginFacebook, ICallback<LoginResponse> iCallback) {
        super(iCallback);
        this.loginFacebook = loginFacebook;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "LoginFacebookRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "LoginFacebookRequest SUCCESS: " + responseString);
            LoginResponse loginResponse = new Gson().fromJson(responseString, LoginResponse.class);
            Logger.debug(getClass(), "LoginFacebookRequest getFacebookId: " + loginResponse.getFacebookId());
            Logger.debug(getClass(), "LoginFacebookRequest getFacebookToken: " + loginResponse.getToken());
            Logger.debug(getClass(), "LoginFacebookRequest getSocial: " + loginResponse.getSocial());
            onSuccess(loginResponse);
        } else {
            Logger.error(getClass(), "LoginFacebookRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getLoginByFacebookUrl();
    }

    @Override
    protected String buildRequestBody() {
        try {
            return new Gson().toJson(loginFacebook);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
