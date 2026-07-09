package com.example.ms_notificacoes.dto;

import java.math.BigDecimal;

public record EmailServiceDto(String nomeOrigem,
                              String emailOrigem,
                              String nomeDestino,
                              String emailDestino,
                              BigDecimal valor) {
}
