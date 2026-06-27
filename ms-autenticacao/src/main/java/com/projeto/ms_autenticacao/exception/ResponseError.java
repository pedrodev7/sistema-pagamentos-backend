package com.projeto.ms_autenticacao.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseError(String message, HttpStatus httpStatus, LocalDateTime localDateTime) {
}
