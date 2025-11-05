package by.antohakon.outboxpattern.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentStatus {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(
            topics = "${kafka.topic.one}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listenPaymentStatusSucess(String message) {

        log.info(message);

    }

    @KafkaListener(
            topics = "${kafka.topic.two}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listenPaymentStatusFail(String message) {

        log.info(message);

    }

}
