package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.Login;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.google.gson.Gson;

import static com.example.myconnections_android.api.ApiUrl.getLoginUrl;

/**
 * Created by kvarivoda on 15.04.2016.
 */
public class LoginRequest extends PostConnection<LoginResponse> {
    private Login login;

    public LoginRequest(Login login, ICallback<LoginResponse> iCallback) {
        super(iCallback);
        this.login = login;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "LoginRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "LoginRequest SUCCESS: " + responseString);
            LoginResponse loginResponse = new Gson().fromJson(responseString, LoginResponse.class);
            onSuccess(loginResponse);
        } else {
            Logger.error(getClass(), "LoginRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getLoginUrl();
    }

    @Override
    protected String buildRequestBody() {
        try {
            return new Gson().toJson(login);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
