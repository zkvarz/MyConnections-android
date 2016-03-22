package com.example.myconnections_android.core.structure.observer;


import com.example.myconnections_android.core.structure.models.error.IError;

public interface IObserver<T> {

    void onObserved(T t);

    void onNothing(IError error);
}
