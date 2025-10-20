package com.epis.services.exception;

public class MovimentacaoNaoEncontradaException extends RuntimeException {
    public MovimentacaoNaoEncontradaException(String message) {
        super(message);
    }
}
