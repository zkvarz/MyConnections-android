package com.example.myconnections_android.core.structure.requests.models;

import com.example.myconnections_android.core.structure.helpers.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * response from HttpRequest
 */
public class RemoteResponse {
    private final InputStream mStream;
    private final int mResponseCode;

    public RemoteResponse(InputStream mStream, int mResponseCode) {
        this.mStream = mStream;
        this.mResponseCode = mResponseCode;
    }

    public InputStream getInputStream() {
        return mStream;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public boolean isSuccess() {
        return getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    @Override
    public String toString() {
        try {
            return IOUtils.toString(mStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
