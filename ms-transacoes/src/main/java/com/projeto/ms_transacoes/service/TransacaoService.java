package com.projeto.ms_transacoes.service;

import com.projeto.ms_transacoes.domain.Conta;
import com.projeto.ms_transacoes.domain.StatusTransacao;
import com.projeto.ms_transacoes.domain.Transacao;
import com.projeto.ms_transacoes.dto.TransferenciaRequestDTO;
import com.projeto.ms_transacoes.exception.ContaNaoEncontradaException;
import com.projeto.ms_transacoes.exception.SaldoInsuficienteException;
import com.projeto.ms_transacoes.repository.ContaRepository;
import com.projeto.ms_transacoes.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransacaoService {
    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public TransacaoService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public Transacao realizarTransferencia(TransferenciaRequestDTO transferenciaRequestDTO){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long usuarioId = Long.parseLong(authentication.getName());

        Conta contaOrigem = this.getContaByUsuarioId(usuarioId);
        Conta contaDestino = this.getContaByUsuarioId(transferenciaRequestDTO.contaDestinoId());

        if(contaOrigem.getId().equals(contaDestino.getId())){
            salvarTransacao(contaOrigem, contaDestino, transferenciaRequestDTO.valor(), StatusTransacao.FALHA);
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        if(contaOrigem.getSaldo().compareTo(transferenciaRequestDTO.valor()) < 0){
            salvarTransacao(contaOrigem, contaDestino, transferenciaRequestDTO.valor(), StatusTransacao.FALHA);
            throw new SaldoInsuficienteException("Saldo Insuficiente para Realizar a Transferencia");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaRequestDTO.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaRequestDTO.valor()));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        return salvarTransacao(contaOrigem, contaDestino, transferenciaRequestDTO.valor(), StatusTransacao.SUCESSO);
    }

    private Conta getContaByUsuarioId(Long usuarioId) {
        return contaRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta de usuario não encontrada: " + usuarioId));
    }

    private Transacao salvarTransacao(Conta contaOrigem, Conta contaDestino, BigDecimal valor, StatusTransacao status) {
        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(valor);
        transacao.setStatus(status);
        transacao.setDataOperacao(LocalDateTime.now());

        return transacaoRepository.save(transacao);
    }
}
