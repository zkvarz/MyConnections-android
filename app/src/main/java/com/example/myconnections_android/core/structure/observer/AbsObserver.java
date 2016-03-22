package com.example.myconnections_android.core.structure.observer;


import com.example.myconnections_android.core.structure.models.error.IError;

public class AbsObserver<T> implements IObserver<T> {

    @Override
    public void onObserved(T t) {
        onAnyResponse();
    }

    @Override
    public void onNothing(IError error) {
        onAnyResponse();
    }


    public void onAnyResponse() {
    }
}
