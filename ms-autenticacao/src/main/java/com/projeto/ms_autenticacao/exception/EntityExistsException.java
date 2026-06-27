package com.projeto.ms_autenticacao.exception;

public class EntityExistsException extends RuntimeException{
    public EntityExistsException(String message) {
        super(message);
    }
}
