package com.projeto.ms_autenticacao.mensaging.producer;

import com.projeto.ms_autenticacao.domain.Usuario;
import com.projeto.ms_autenticacao.dto.RegisterRequestDto;
import com.projeto.ms_autenticacao.dto.UsuarioEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UsuarioProducer {
    private final KafkaTemplate<String, UsuarioEvent> kafkaTemplate;

    public UsuarioProducer(KafkaTemplate<String, UsuarioEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarMensagem(Usuario usuario, RegisterRequestDto registerRequestDto){
        UsuarioEvent usuarioEvent = new UsuarioEvent(usuario.getId(), registerRequestDto.email(), registerRequestDto.nome());

        kafkaTemplate.send("usuario-cadastrado-topic", usuarioEvent);
    }
}
