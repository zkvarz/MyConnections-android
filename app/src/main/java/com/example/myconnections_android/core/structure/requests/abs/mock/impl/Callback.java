package com.example.myconnections_android.core.structure.requests.abs.mock.impl;


import com.alshvets.core.structure.models.error.IError;
import com.alshvets.core.structure.requests.mock.ICallback;

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
