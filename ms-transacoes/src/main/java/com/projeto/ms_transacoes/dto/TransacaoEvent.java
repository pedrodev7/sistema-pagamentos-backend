package com.projeto.ms_transacoes.dto;

import java.math.BigDecimal;

public record TransacaoEvent(Long usuarioOrigemId,
                             Long usuarioDestinoId,
                             BigDecimal valor) {
}
