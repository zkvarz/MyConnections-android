package com.example.myconnections_android.core.structure.models.result;


import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.models.model.IModel;

public class Result<T extends IModel> implements IResult<T> {

    private final T mModel;
    private final IError mError;

    public Result(T mModel, IError mError) {
        this.mModel = mModel;
        this.mError = mError;
    }

    public Result(T mModel) {
        this(mModel, null);
    }

    public T getModel() {
        return mModel;
    }

    @Override
    public boolean isSuccess() {
        return mError==null;
    }

    public IError getError() {
        return mError;
    }
}
