package com.example.myconnections_android.core.structure.requests.abs.mock.impl;


import com.alshvets.core.structure.requests.mock.IWorker;

public abstract class Worker<T> implements IWorker<T> {

    @Override
    public void doWork() {
        before();
        work();
        after();
    }

    protected abstract void after();

    protected abstract void work();

    protected abstract void before();
}
