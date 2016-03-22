package com.example.myconnections_android.core.structure.manager;

import android.os.Handler;
import android.os.Looper;

import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.observer.IObserver;

import java.util.ArrayList;

public abstract class AbsManager<T> implements IManager<T> {

    /**
     * The list of observers.  An observer can be in the list at most
     * once and will never be null.
     */
    protected final ArrayList<IObserver<T>> mObservers = new ArrayList<>();

    protected AbsManager() {

    }

    @Override
    public void registerObserver(IObserver<T> observer) {
        synchronized (mObservers) {
            if (!mObservers.contains(observer)) {
                mObservers.add(observer);
            }
        }
    }

    @Override
    public void unregisterAll() {
        synchronized (mObservers) {
            mObservers.clear();
        }
    }

    @Override
    public void unregisterObserver(IObserver<T> observer) {
        synchronized (mObservers) {
            int index = mObservers.indexOf(observer);
            if (index != -1) {
                mObservers.remove(index);
            }
        }
    }


    @Override
    public void notifySuccess(final T result) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (IObserver<T> observer : mObservers) {
                    if (observer != null) {
                        observer.onObserved(result);
                    }
                }
            }
        });
    }

    public void notifyFail(final IError error) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                for (IObserver<T> observer : mObservers) {
                    if (observer != null) {
                        observer.onNothing(error);
                    }
                }
            }
        });
    }

}

