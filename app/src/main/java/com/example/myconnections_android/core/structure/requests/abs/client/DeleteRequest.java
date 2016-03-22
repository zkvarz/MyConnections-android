package com.example.myconnections_android.core.structure.requests.abs.client;


import com.example.myconnections_android.core.structure.requests.abs.client.base.QueryRequest;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;

public abstract class DeleteRequest<T> extends QueryRequest<T> {

    public DeleteRequest(ICallback<T> callback) {
        super(callback);
    }

    @Override
    protected final HttpRequestFactory.HttpMethod getHttpMethod() {
        return HttpRequestFactory.HttpMethod.DELETE;
    }


}
