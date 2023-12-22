package com.glady.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GladyException extends Exception {

    public GladyException(String message, Throwable cause){
        super(message, cause);
    }

    public GladyException(String message){
        super(message);
    }
}
