package com.projeto.ms_transacoes.service;

import com.projeto.ms_transacoes.domain.Conta;
import com.projeto.ms_transacoes.domain.StatusTransacao;
import com.projeto.ms_transacoes.domain.Transacao;
import com.projeto.ms_transacoes.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RegistroTransacaoService {
    private final TransacaoRepository transacaoRepository;

    public RegistroTransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transacao salvarTransacao(Conta contaOrigem, Conta contaDestino, BigDecimal valor, StatusTransacao status) {
        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(valor);
        transacao.setStatus(status);
        transacao.setDataOperacao(LocalDateTime.now());

        return transacaoRepository.save(transacao);
    }
}
