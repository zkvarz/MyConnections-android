package com.example.myconnections_android.api.responses;

/**
 * Created by kvarivoda on 22.03.2016.
 */
public class ErrorResponse {

    private ApiError error;

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
