package com.example.myconnections_android.core.structure.observer;


import com.example.myconnections_android.core.structure.models.result.Result;

public class Observer<T extends Result> extends AbsObserver<T> {

    @Override
    public void onObserved(T t) {
        if (!t.isSuccess()) {
            onNothing(t.getError());
        } else {
            onAnyResponse();
        }
    }
}
