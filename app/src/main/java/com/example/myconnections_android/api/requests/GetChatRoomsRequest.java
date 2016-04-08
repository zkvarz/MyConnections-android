package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.Session;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.example.myconnections_android.model.ChatRoom;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.myconnections_android.api.ApiUrl.getChatRoomsUrl;

/**
 * Created by kvarivoda on 08.04.2016.
 */
public class GetChatRoomsRequest extends PostConnection<ArrayList<ChatRoom>> {

    private Session session;

    public GetChatRoomsRequest(Session session, ICallback<ArrayList<ChatRoom>> iCallback) {
        super(iCallback);
        this.session = session;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "GetChatRoomsRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "GetChatRoomsRequest SUCCESS: " + responseString);
            ArrayList<ChatRoom> chatRoom = new Gson().fromJson(responseString, new TypeToken<List<ChatRoom>>() {
            }.getType());
            onSuccess(chatRoom);
        } else {
            Logger.error(getClass(), "GetChatRoomsRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getChatRoomsUrl();
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
