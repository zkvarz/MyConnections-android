package com.example.myconnections_android.core.structure.models.error;

public class StringError implements IError {

    private final String mErrorMessage;

    public StringError(String mErrorMessage) {
        this.mErrorMessage = mErrorMessage;
    }

    @Override
    public String getErrorMessage() {
        return mErrorMessage;
    }
}
