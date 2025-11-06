package com.epis.exception;

import com.epis.services.exception.*;
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

    @ExceptionHandler(FuncaoNaoEncontradaException.class)
    public ResponseEntity<ErrorMessage> handleFuncaoNaoEncontradaException(FuncaoNaoEncontradaException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(UniformeNaoEncontradoException.class)
    public ResponseEntity<ErrorMessage> handleUniformeNaoEncontradoException(UniformeNaoEncontradoException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(ErroInserirDynamoException.class)
    public ResponseEntity<ErrorMessage> handleErroInserirDynamoException(ErroInserirDynamoException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ErroBuscarDynamoException.class)
    public ResponseEntity<ErrorMessage> handleErroBuscarDynamoException(ErroBuscarDynamoException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ErroDeletarDynamoException.class)
    public ResponseEntity<ErrorMessage> handleErroDeletarDynamoException(ErroDeletarDynamoException ex, HttpServletRequest request){
        ErrorMessage error = new ErrorMessage(
                request.getRequestURI(),
                request.getMethod(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
