package com.example.myconnections_android.core.structure.requests.abs.connection;

import android.os.Handler;
import android.os.Looper;

import com.example.myconnections_android.core.structure.models.error.ExceptionError;
import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.models.error.StringError;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.mock.IWorker;

/**
 * Created by kvarivoda on 23.03.2016.
 */
public abstract class ConnectionRequest<T> implements ICallback<T>, Runnable, IWorker {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private ICallback<T> callback;

    public ConnectionRequest(ICallback<T> callback) {
        this.callback = callback;
    }

    public void execute() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            doWork();
        } catch (Exception e) {
            onError(new StringError(e.toString()));
        }
    }


    @Override
    public void onSuccess(final T t) {
        if (callback == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(t);
            }
        });
    }

    @Override
    public void onError(final IError ierror) {
        if (callback == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(ierror);
            }
        });
    }

    protected void onError(Exception e) {
        onError(new ExceptionError(e));
    }

}
