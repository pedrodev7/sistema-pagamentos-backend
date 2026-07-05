package com.projeto.ms_transacoes.messaging.consumer;

import com.projeto.ms_transacoes.domain.Conta;
import com.projeto.ms_transacoes.dto.UsuarioEvent;
import com.projeto.ms_transacoes.repository.ContaRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UsuarioConsumer {
    private final ContaRepository contaRepository;

    public UsuarioConsumer(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @KafkaListener(topics = "usuario-cadastrado-topic", groupId = "transacoes-group")
    public void criarContaTransacao(UsuarioEvent usuarioEvent){
        Conta conta = new Conta();
        conta.setUsuarioId(usuarioEvent.id());
        conta.setSaldo(new BigDecimal(100));
        conta.setEmail(usuarioEvent.email());

        contaRepository.save(conta);

        System.out.println("Conta Criada com Sucesso: " + usuarioEvent.nome());
    }
}
