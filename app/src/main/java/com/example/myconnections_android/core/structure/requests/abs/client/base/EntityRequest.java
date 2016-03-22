package com.example.myconnections_android.core.structure.requests.abs.client.base;


import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;


public abstract class EntityRequest<T> extends HttpRequest<T> {

    public EntityRequest(ICallback<T> callback) {
        super(callback);
    }

    protected void prepare(HttpEntityEnclosingRequest requestBase){
        HttpEntity entity = buildEntity();
        try {
            if (entity != null) {
                requestBase.setEntity(entity);//new StringEntity(postContents, HTTP.UTF_8)
            }
        } catch (Exception e) {
            Logger.error(getClass(), "Error setting up post body", e);
        }

    }
}
