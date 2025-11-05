package by.antohakon.paymentservice.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @KafkaListener(
            topics = "${kafka.topic.three}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listenOrder(String order) {

        System.out.println(order);

    }

}
