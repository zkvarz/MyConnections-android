package com.example.myconnections_android.core.structure.requests.mock;

import com.example.myconnections_android.core.structure.models.error.IError;

/**
 * The callback for  background Request
 *
 * @param <T> - type of Returned value
 */
public interface ICallback<T> {

    void onSuccess(T t);

    void onError(IError error);
}
