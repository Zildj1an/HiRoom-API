package com.cgaxtr.hiroom.Exceptions;

public class InvalidCredentials extends Exception {

    public InvalidCredentials(){
        super();
    }

    public InvalidCredentials(String message){
        super(message);
    }

    public InvalidCredentials(String message, Throwable cause){
        super(message,cause);
    }

    public InvalidCredentials(Throwable cause) {
        super(cause);
    }
}
