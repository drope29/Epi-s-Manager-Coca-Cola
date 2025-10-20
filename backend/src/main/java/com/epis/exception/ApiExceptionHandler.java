package com.epis.exception;

import com.epis.services.exception.EpiNaoEncontradoException;
import com.epis.services.exception.FuncionarioNaoEncontradoException;
import com.epis.services.exception.MovimentacaoNaoEncontradaException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(FuncionarioNaoEncontradoException.class)
    public ResponseEntity<ErrorMessage> handleFuncionarioNaoEncontradoException(FuncionarioNaoEncontradoException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EpiNaoEncontradoException.class)
    public ResponseEntity<ErrorMessage> handleEpiNaoEncontradoException(EpiNaoEncontradoException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MovimentacaoNaoEncontradaException.class)
    public ResponseEntity<ErrorMessage> handleMovimentacaoNaoEncontradaException(MovimentacaoNaoEncontradaException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
