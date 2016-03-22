package com.example.myconnections_android.core.structure.models.model;

import android.os.Parcel;


public class Model implements IModel {


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Model() {
    }

    private Model(Parcel in) {
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        public Model createFromParcel(Parcel source) {
            return new Model(source);
        }

        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
}
