package com.example.myconnections_android.core.structure.requests.abs;

import android.os.Handler;
import android.os.Looper;

import com.example.myconnections_android.core.structure.helpers.IOUtils;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.models.error.ExceptionError;
import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.models.error.StringError;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.core.structure.requests.mock.IWorker;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract class for background work
 *
 * @param <T> - type of Returned value
 */
public abstract class Request<T> implements Runnable, IWorker<T>,
        ICallback<T> {

    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private static final ExecutorService service = Executors.newCachedThreadPool();

    private ICallback<T> mCallback;

    public Request(ICallback<T> callback) {
        this.setCallback(callback);
        // mExecutor = new Thread(this);
    }

    /**
     * Method that starts background work
     */
    public void execute() {
        new Thread(this).start();
        //  service.execute(this);
        // mExecutor.start();
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
        //  Logger.debug(getClass(), "onSuccess");
        if (getCallback() == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getCallback() != null) {
                    getCallback().onSuccess(t);
                }
                setCallback(null);
            }
        });
    }

    @Override
    public void onError(final IError error) {
        Logger.debug(getClass(), "onError: " + error.getErrorMessage());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (getCallback() != null) {
                    getCallback().onError(error);
                }
                setCallback(null);
            }
        });
    }

    public ICallback<T> getCallback() {
        return mCallback;
    }

    private void setCallback(ICallback<T> callback) {
        this.mCallback = callback;
    }

    public void removeCallback() {
        setCallback(null);
    }

    protected String toString(InputStream is) throws IOException {
        return IOUtils.toString(is);
    }

    protected void notifyError(String error) {
        Logger.debug(getClass(), error);
        onError(new StringError(error));
    }

    protected void onError(Exception e) {
        onError(new ExceptionError(e));
    }

}
