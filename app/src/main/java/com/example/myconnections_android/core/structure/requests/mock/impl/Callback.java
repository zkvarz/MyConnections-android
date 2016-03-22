package com.example.myconnections_android.core.structure.requests.mock.impl;

import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;

public class Callback<T> implements ICallback<T> {

    @Override
    public void onSuccess(T t) {
        onAnyResponse();
    }

    public void onAnyResponse() {

    }

    @Override
    public void onError(IError error) {
        onAnyResponse();
    }
}
