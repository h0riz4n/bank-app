package ru.yandex.cash_service.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import ru.notification.model.NotificationDto;

@Configuration
public class KafkaConfiguration {

    @Bean
    public KafkaTemplate<String, Object> kafkaHouseTemplate(
        @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        DefaultKafkaProducerFactory<String, Object> producerFactory = 
            new DefaultKafkaProducerFactory<>(
                getProducerConfig(bootstrapServers), 
                new StringSerializer(),
                new DelegatingByTypeSerializer(
                    Map.of(byte[].class, new ByteArraySerializer(), NotificationDto.class, new JsonSerializer<Object>())
                ));
        return new KafkaTemplate<>(producerFactory);
    }


    private Map<String, Object> getProducerConfig(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();

        // Server address
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Serialization properties
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return props;
    }

}