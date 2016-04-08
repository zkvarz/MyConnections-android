package com.example.myconnections_android.api.requests;

import com.example.myconnections_android.api.models.MessageSend;
import com.example.myconnections_android.api.responses.ErrorResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.abs.connection.PostConnection;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.models.RemoteResponse;
import com.example.myconnections_android.model.Message;
import com.google.gson.Gson;

import static com.example.myconnections_android.api.ApiUrl.getSendMessageUrl;

/**
 * Created by kvarivoda on 08.04.2016.
 */
public class SendMessageRequest extends PostConnection<Message> {
    private MessageSend messageSend;

    public SendMessageRequest(MessageSend messageSend, ICallback<Message> iCallback) {
        super(iCallback);
        this.messageSend = messageSend;
    }

    @Override
    protected void parseResponse(RemoteResponse remoteResponse) {
        String responseString = remoteResponse.toString();
        Logger.debug(getClass(), "SendMessageRequest responseString: " + responseString);
        if (remoteResponse.isSuccess()) {
            Logger.debug(getClass(), "SendMessageRequest SUCCESS: " + responseString);
            Message message = new Gson().fromJson(responseString, Message.class);
            onSuccess(message);
        } else {
            Logger.error(getClass(), "SendMessageRequest ERROR: " + responseString);
            ErrorResponse apiError = new Gson().fromJson(responseString, ErrorResponse.class);
            onError(apiError.getError());
        }
    }

    @Override
    protected String buildUrl() {
        return getSendMessageUrl();
    }

    @Override
    protected String buildRequestBody() {
        try {
            Logger.debug(getClass(), "JSON " + new Gson().toJson(messageSend));
            return new Gson().toJson(messageSend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
