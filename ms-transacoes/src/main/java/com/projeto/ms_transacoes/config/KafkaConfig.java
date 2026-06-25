package com.projeto.ms_transacoes.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic transferenciaTopic() {
        return TopicBuilder.name("transferencia-realizada-topic")
                // Como estamos rodando apenas 1 broker (nó) localmente, partições e réplicas ficam em 1
                .partitions(1)
                .replicas(1)
                .build();
    }

}
