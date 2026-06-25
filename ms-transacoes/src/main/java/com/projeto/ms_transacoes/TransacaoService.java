package com.projeto.ms_transacoes;

import com.projeto.ms_transacoes.domain.Conta;
import com.projeto.ms_transacoes.domain.StatusTransacao;
import com.projeto.ms_transacoes.domain.Transacao;
import com.projeto.ms_transacoes.dto.TransferenciaRequestDTO;
import com.projeto.ms_transacoes.exception.ContaNaoEncontradaException;
import com.projeto.ms_transacoes.exception.SaldoInsuficienteException;
import com.projeto.ms_transacoes.repository.ContaRepository;
import com.projeto.ms_transacoes.repository.TransacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        Conta contaOrigem = contaRepository.findByUsuarioId(transferenciaRequestDTO.contaOrigemId())
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta de Origem não Encontrada"));
        Conta contaDestino = contaRepository.findByUsuarioId(transferenciaRequestDTO.contaDestinoId())
                .orElseThrow(() -> new ContaNaoEncontradaException("Conta de Destino não Encontrada"));

        if(contaOrigem.getSaldo().compareTo(transferenciaRequestDTO.valor()) < 0){
            throw new SaldoInsuficienteException("Saldo Insuficiente para Realizar a Transferencia");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo().subtract(transferenciaRequestDTO.valor()));
        contaDestino.setSaldo(contaDestino.getSaldo().add(transferenciaRequestDTO.valor()));

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacao = new Transacao();
        transacao.setContaOrigem(contaOrigem);
        transacao.setContaDestino(contaDestino);
        transacao.setValor(transferenciaRequestDTO.valor());
        transacao.setStatus(StatusTransacao.SUCESSO);
        transacao.setDataOperacao(LocalDateTime.now());

        return transacaoRepository.save(transacao);
    }
}
