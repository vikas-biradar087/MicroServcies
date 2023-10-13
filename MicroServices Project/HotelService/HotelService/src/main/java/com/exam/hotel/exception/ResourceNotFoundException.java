package com.exam.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{


    public ResourceNotFoundException(String s){
        super(s);

    }

    public ResourceNotFoundException(){
        super("Resource Not Found !!");
    }

}
