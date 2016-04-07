package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.Session;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.google.gson.Gson;

import static com.example.myconnections_android.api.ApiUrl.getLoginByTwitterUrl;

/**
 * Created by kvarivoda on 31.03.2016.
 */
public class LoginTwitterRequest extends PostConnection<LoginResponse> {
    private Session session;

    public LoginTwitterRequest(Session session, ICallback<LoginResponse> iCallback) {
        super(iCallback);
        this.session = session;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "LoginTwitterRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "LoginTwitterRequest SUCCESS: " + responseString);
            LoginResponse loginResponse = new Gson().fromJson(responseString, LoginResponse.class);
            onSuccess(loginResponse);
        } else {
            Logger.error(getClass(), "LoginTwitterRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getLoginByTwitterUrl();
    }

    @Override
    protected String buildRequestBody() {
        try {
            return new Gson().toJson(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
