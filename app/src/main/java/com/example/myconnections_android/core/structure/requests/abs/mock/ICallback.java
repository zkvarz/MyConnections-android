package com.example.myconnections_android.core.structure.requests.abs.mock;


import com.alshvets.core.structure.models.error.IError;

/**
 * The callback for  background Request
 *
 * @param <T> - type of Returned value
 */
public interface ICallback<T> {

    void onSuccess(T t);

    void onError(IError error);
}
