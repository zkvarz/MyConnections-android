package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.Session;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.api.responses.UsersResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.myconnections_android.api.ApiUrl.getUsersUrl;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public class GetUsersRequest extends PostConnection<ArrayList<UsersResponse>> {

    private Session session;

    public GetUsersRequest(Session session, ICallback<ArrayList<UsersResponse>> iCallback) {
        super(iCallback);
        this.session = session;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "LoginFacebookRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "LoginFacebookRequest SUCCESS: " + responseString);
            ArrayList<UsersResponse> usersResponse = new Gson().fromJson(responseString, new TypeToken<List<UsersResponse>>(){}.getType());
            onSuccess(usersResponse);
        } else {
            Logger.error(getClass(), "LoginFacebookRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getUsersUrl();
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
