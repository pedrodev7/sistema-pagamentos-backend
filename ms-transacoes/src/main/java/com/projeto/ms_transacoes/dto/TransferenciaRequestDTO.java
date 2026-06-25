package com.projeto.ms_transacoes.dto;

import java.math.BigDecimal;

public record TransferenciaRequestDTO(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
}
