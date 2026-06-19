package com.rba.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rba.services.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardStatusConsumer {

    private final ObjectMapper objectMapper;

    private final UserService userService;

    @KafkaListener(
            topics = "${app.kafka.card-status-topic}",
            groupId = "${spring.kafka.consumer.group-id}",
            autoStartup = "${app.kafka.enabled:false}"
    )
    public void consume(ConsumerRecord<String, String> record) {
        String payload = record.value();

        log.info("Message received: {}", payload);

        try {
            CardStatusMessage message = objectMapper.readValue(payload, CardStatusMessage.class);

            validateMessage(message);

            userService.updateCardStatusByIdentficationNumber(message.getOib(), message.getStatus());

            log.info("Card status updated successfully. OIB: {}, status: {}", message.getOib(), message.getStatus());

        } catch (Exception ex) {
            log.error("Error processing Kafka message: {}", payload, ex);
        }
    }

    private void validateMessage(CardStatusMessage message) {
        if (message.getOib() == null || message.getOib().isBlank()) {
            throw new IllegalArgumentException("OIB cannot be empty.");
        }

        if (message.getStatus() == null || message.getStatus().isBlank()) {
            throw new IllegalArgumentException("Card status cannot be empty.");
        }
    }
}