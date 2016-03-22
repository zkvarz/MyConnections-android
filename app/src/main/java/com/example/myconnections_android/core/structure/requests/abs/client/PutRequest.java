package com.example.myconnections_android.core.structure.requests.abs.client;


import com.example.myconnections_android.core.structure.requests.abs.client.base.EntityRequest;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;

public abstract class PutRequest<T> extends EntityRequest<T> {

    public PutRequest(ICallback<T> callback) {
        super(callback);
    }

    @Override
    protected final HttpRequestFactory.HttpMethod getHttpMethod() {
        return HttpRequestFactory.HttpMethod.PUT;
    }
}
