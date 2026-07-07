package com.example.ms_notificacoes.mensaging;

import com.example.ms_notificacoes.domain.UsuarioContato;
import com.example.ms_notificacoes.dto.UsuarioEvent;
import com.example.ms_notificacoes.repository.UsuarioContatoRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UsuarioCadastradoConsumer {

    private final UsuarioContatoRepository usuarioContatoRepository;

    public UsuarioCadastradoConsumer(UsuarioContatoRepository usuarioContatoRepository) {
        this.usuarioContatoRepository = usuarioContatoRepository;
    }

    @KafkaListener(topics = "usuario-cadastrado-topic", groupId = "notificacoes-group")
    public void salvarContato(UsuarioEvent usuarioEvent){
        UsuarioContato usuarioContato = new UsuarioContato(usuarioEvent.id(), usuarioEvent.nome(), usuarioEvent.email());
        usuarioContatoRepository.save(usuarioContato);
    }
}
