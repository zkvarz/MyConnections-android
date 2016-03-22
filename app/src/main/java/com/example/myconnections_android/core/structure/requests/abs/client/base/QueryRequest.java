package com.example.myconnections_android.core.structure.requests.abs.client.base;


import com.example.myconnections_android.core.structure.requests.mock.ICallback;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;

import java.net.URLEncoder;
import java.util.Map;

public abstract class QueryRequest<T> extends HttpRequest<T> {


    public QueryRequest(ICallback<T> callback) {
        super(callback);
    }

    @Override
    protected String prepareUrl(String url) {
        String preparedUrl=super.prepareUrl(url);
        Map<String, String> getData=buildGetParams();
        if (getData != null) {
            for (String key : getData.keySet()) {
                try {
                    String encodedParam= URLEncoder.encode(getData.get(key), HTTP.UTF_8);
                    if (preparedUrl.contains("?")) {
                        preparedUrl += "&";
                    } else {
                        preparedUrl += "?";
                    }
                    preparedUrl += key + "=" + encodedParam;
                }catch (Exception e) {
                   //empty
                }
            }
        }
        return preparedUrl;
    }

    protected Map<String, String> buildGetParams() {
        return null;
    }

    @Override
    protected final HttpEntity buildEntity() {
        return null;
    }
}
