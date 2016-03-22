package com.example.myconnections_android.core.structure.manager.comunication.models;

public class CommunicationException extends Exception {

    private static final long serialVersionUID = 8665847454939007794L;
    private final String message;

    public CommunicationException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}