package com.projeto.ms_transacoes.messaging.producer;

import com.projeto.ms_transacoes.dto.TransacaoEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransacaoProducer {

    private final KafkaTemplate<String, TransacaoEvent> kafkaTemplate;

    public TransacaoProducer(KafkaTemplate<String, TransacaoEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarNotificacaoDeTransferencia(TransacaoEvent transacaoEvent){
        kafkaTemplate.send("transferencia-realizada-topic", transacaoEvent);
    }
}
