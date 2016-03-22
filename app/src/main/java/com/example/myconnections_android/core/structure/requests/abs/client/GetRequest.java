package com.example.myconnections_android.core.structure.requests.abs.client;

import com.example.myconnections_android.core.structure.requests.abs.client.base.QueryRequest;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;

public abstract class GetRequest<T> extends QueryRequest<T> {


    public GetRequest(ICallback<T> callback) {
        super(callback);
    }

    @Override
    protected final HttpRequestFactory.HttpMethod getHttpMethod() {
        return HttpRequestFactory.HttpMethod.GET;
    }

}
