package com.example.myconnections_android.core.structure.models.error;

public class ExceptionError implements IError {

    private final Exception exception;

    public ExceptionError(Exception exception) {
        this.exception = exception;
    }

    @Override
    public String getErrorMessage() {
        return exception.getMessage();
    }
}
