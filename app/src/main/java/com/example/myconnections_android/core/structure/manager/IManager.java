package com.example.myconnections_android.core.structure.manager;


import com.example.myconnections_android.core.structure.observer.IObserver;

public interface IManager<T> {

    void obtainModel();

    /**
     * Adds an observer to the list. The observer cannot be null and it must not already
     * be registered.
     * @param observer the observer to register
     */
    void registerObserver(IObserver<T> observer);

    void notifySuccess(final T result);

    /**
     * Removes a previously registered observer. The observer must not be null and it
     * must already have been registered.
     * @param observer the observer to unregister
     */
    void unregisterObserver(IObserver<T> observer);

    /**
     * Remove all registered observers.
     */
    void unregisterAll();

}
