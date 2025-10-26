package com.epis.services.exception;

public class ErroBuscarDynamoException extends RuntimeException {
    public ErroBuscarDynamoException(String message) {
        super(message);
    }
}
