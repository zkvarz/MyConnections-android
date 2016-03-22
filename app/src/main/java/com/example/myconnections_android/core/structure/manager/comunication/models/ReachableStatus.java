package com.example.myconnections_android.core.structure.manager.comunication.models;

import com.example.myconnections_android.core.structure.models.result.IResult;

public enum ReachableStatus implements IResult {
    HOST_REACHABLE(true), HOST_NOT_REACHABLE(false);

    private final boolean mIsSuccess;

    ReachableStatus(boolean mIsSuccess) {
        this.mIsSuccess = mIsSuccess;
    }

    @Override
    public boolean isSuccess() {
        return mIsSuccess;
    }


}
