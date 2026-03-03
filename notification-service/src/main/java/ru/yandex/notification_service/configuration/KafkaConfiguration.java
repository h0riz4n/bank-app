package ru.yandex.notification_service.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.DefaultKafkaHeaderMapper;
import org.springframework.kafka.support.converter.MessagingMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import ru.yandex.notification_service.model.dto.NotificationDto;

@EnableKafka
@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic notifications() {
        return TopicBuilder.name("notifications")
            .partitions(3)
            .replicas(1)
            .compact()
            .build();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, NotificationDto> houseRetryContainerFactory(
        MessagingMessageConverter messagingMessageConverter,
        @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers
    ) {
        ConcurrentKafkaListenerContainerFactory<String, NotificationDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        ConsumerFactory<String, NotificationDto> consumerFactory =  new DefaultKafkaConsumerFactory<>(
            getConsumerConfig(bootstrapServers), 
            new ErrorHandlingDeserializer<String>(), 
            new ErrorHandlingDeserializer<NotificationDto>()
        );

        factory.setConsumerFactory(consumerFactory);
        factory.setRecordMessageConverter(messagingMessageConverter);
        return factory;
    }

    /* Сохранить как пример настройки маппинга заголовков */
    @Bean
    public MessagingMessageConverter converter() {
        MessagingMessageConverter converter = new MessagingMessageConverter();
        DefaultKafkaHeaderMapper defaultKafkaHeaderMapper = new DefaultKafkaHeaderMapper();
        defaultKafkaHeaderMapper.addRawMappedHeader("Custom", true);
        defaultKafkaHeaderMapper.setEncodeStrings(true);
        converter.setHeaderMapper(defaultKafkaHeaderMapper);
        return converter;
    }

    private Map<String, Object> getConsumerConfig(String bootstrapServers) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.yandex.notification_service.model.dto");
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.yandex.notification_service.model.dto.NotificationDto");
        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "bank-app");
        return props;
    }
}
