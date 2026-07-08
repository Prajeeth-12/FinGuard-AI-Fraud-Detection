package com.rohith.config;

import com.rohith.dto.RiskEvaluation;
import com.rohith.model.Transaction;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")

    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, RiskEvaluation>producerFactory()
    {
        Map<String,Object> configprops=new HashMap<>();

        configprops.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
        configprops.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configprops.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configprops);
    }
    @Bean
    public KafkaTemplate<String,RiskEvaluation>kafkaTemplate()
    {

        return new KafkaTemplate<>(producerFactory());
    }
}
