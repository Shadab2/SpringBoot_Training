package com.oracle.oracle.training.exceptions;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String errorMessage){
        super(errorMessage);
    }
}
