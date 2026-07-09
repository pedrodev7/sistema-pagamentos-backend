package com.example.ms_notificacoes.mensaging;

import com.example.ms_notificacoes.domain.UsuarioContato;
import com.example.ms_notificacoes.dto.EmailServiceDto;
import com.example.ms_notificacoes.dto.TransacaoEvent;
import com.example.ms_notificacoes.dto.UsuarioEvent;
import com.example.ms_notificacoes.repository.UsuarioContatoRepository;
import com.example.ms_notificacoes.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransacaoConsumer {
    private final UsuarioContatoRepository usuarioContatoRepository;
    private final EmailService emailService;

    public TransacaoConsumer(UsuarioContatoRepository usuarioContatoRepository, EmailService emailService) {
        this.usuarioContatoRepository = usuarioContatoRepository;
        this.emailService = emailService;
    }

    @KafkaListener(topics = "transferencia-realizada-topic", groupId = "notificacoes-group")
    public void processarNotificacaoTransacao(TransacaoEvent transacaoEvent){
        UsuarioContato usuarioOrigem = usuarioContatoRepository.findById(transacaoEvent.usuarioOrigemId())
                .orElseThrow(() -> new RuntimeException("Usuário de origem não encontrado"));
        UsuarioContato usuarioDestino = usuarioContatoRepository.findById(transacaoEvent.usuarioDestinoId())
                .orElseThrow(() -> new RuntimeException("Usuário de destino não encontrado"));

        emailService.enviarComprovante(new EmailServiceDto(
                usuarioOrigem.getName(),
                usuarioOrigem.getEmail(),
                usuarioDestino.getName(),
                usuarioDestino.getEmail(),
                transacaoEvent.valor()
        ));
    }
}
