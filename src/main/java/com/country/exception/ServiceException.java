package com.country.exception;

public class ServiceException extends RuntimeException {

    public ServiceException(String message, Exception ex){
        super(message, ex );
    }

    public ServiceException(String message) {
        super(message);
    }
}
