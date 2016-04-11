package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.ChatRoom;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.google.gson.Gson;

import static com.example.myconnections_android.api.ApiUrl.getChatPrivateRoomUrl;

/**
 * Created by kvarivoda on 11.04.2016.
 */
public class GetPrivateChatRoomRequest extends PostConnection<ChatRoom> {

    private LoginResponse loginResponse;

    public GetPrivateChatRoomRequest(LoginResponse loginResponse, ICallback<ChatRoom> iCallback) {
        super(iCallback);
        this.loginResponse = loginResponse;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "GetPrivateChatRoomRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "GetPrivateChatRoomRequest SUCCESS: " + responseString);
            ChatRoom chatRoom = new Gson().fromJson(responseString, ChatRoom.class);
            onSuccess(chatRoom);
        } else {
            Logger.error(getClass(), "GetPrivateChatRoomRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getChatPrivateRoomUrl();
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
