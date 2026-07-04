package com.projeto.ms_transacoes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<?> SaldoInsuficienteException(SaldoInsuficienteException ex){
        ResponseError responseError = new ResponseError(ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<?> ContaNaoEncontradaException(ContaNaoEncontradaException ex) {
        ResponseError responseError = new ResponseError(ex.getMessage(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }
}
