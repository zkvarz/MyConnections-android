package com.example.myconnections_android.api.responses;


import com.example.myconnections_android.core.structure.models.error.IError;

public class ApiError implements IError {


    private ErrorCode code;
    private String message;

    public ErrorCode getCode() {
        return code;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }
}
