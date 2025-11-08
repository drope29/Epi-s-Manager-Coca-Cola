package com.epis.services.exception;

public class ErroDeletarDynamoException extends RuntimeException {
    public ErroDeletarDynamoException(String message) {
        super(message);
    }
}
