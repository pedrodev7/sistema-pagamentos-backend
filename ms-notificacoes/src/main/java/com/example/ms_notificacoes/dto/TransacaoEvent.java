package com.example.ms_notificacoes.dto;

import java.math.BigDecimal;

public record TransacaoEvent(Long usuarioOrigemId, Long usuarioDestinoId, BigDecimal valor) {
}
