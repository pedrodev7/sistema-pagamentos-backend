package com.projeto.ms_transacoes.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ResponseError(String mensage, HttpStatus status, LocalDateTime localDateTime) {
}
