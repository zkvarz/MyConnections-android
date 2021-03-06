package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.google.gson.Gson;

import static com.example.myconnections_android.api.ApiUrl.getUpdateUserUrl;

/**
 * Created by kvarivoda on 24.03.2016.
 */
public class UpdateUserRequest extends PostConnection<LoginResponse> {

    private LoginResponse loginResponse;

    public UpdateUserRequest(LoginResponse loginResponse, ICallback<LoginResponse> iCallback) {
        super(iCallback);
        this.loginResponse = loginResponse;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "UpdateUserRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            LoginResponse loginResponse = new Gson().fromJson(responseString, LoginResponse.class);
            onSuccess(loginResponse);
        } else {
            Logger.error(getClass(), "UpdateUserRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getUpdateUserUrl();
    }

    @Override
    protected String buildRequestBody() {
        try {
            return new Gson().toJson(loginResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
