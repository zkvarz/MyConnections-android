package com.example.myconnections_android.core.structure.requests.abs.connection;


import com.example.myconnections_android.core.structure.requests.mock.ICallback;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public abstract class PostConnection<T> extends HttpUrlConnection<T> {
    public PostConnection(ICallback<T> callback) {
        super(callback);
    }

    @Override
    protected final String getHttpMethod() {
        return HttpUrlConnection.HttpMethod.POST;
    }
}
