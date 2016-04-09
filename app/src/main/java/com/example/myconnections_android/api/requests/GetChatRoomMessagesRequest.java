package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.ObjectId;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.example.myconnections_android.model.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.myconnections_android.api.ApiUrl.getChatRoomMessagesUrl;

/**
 * Created by kvarivoda on 09.04.2016.
 */
public class GetChatRoomMessagesRequest extends PostConnection<ArrayList<Message>> {

    private ObjectId objectId;

    public GetChatRoomMessagesRequest(ObjectId objectId, ICallback<ArrayList<Message>> iCallback) {
        super(iCallback);
        this.objectId = objectId;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "GetChatRoomMessagesRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "GetChatRoomMessagesRequest SUCCESS: " + responseString);
            ArrayList<Message> messages = new Gson().fromJson(responseString, new TypeToken<List<Message>>() {
            }.getType());
            onSuccess(messages);
        } else {
            Logger.error(getClass(), "GetChatRoomMessagesRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getChatRoomMessagesUrl();
    }

    @Override
    protected String buildRequestBody() {
        try {
            return new Gson().toJson(objectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
