package by.antohakon.paymentservice.event;

import by.antohakon.paymentservice.dto.OrderEvent;
import by.antohakon.paymentservice.entity.PaymentOrders;
import by.antohakon.paymentservice.repository.PaymentOrdersRepository;
import by.antohakon.paymentservice.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private final PaymentOrdersRepository paymentOrdersRepository;

    @KafkaListener(
            topics = "${kafka.topic.three}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void listenOrder(String order) {

        System.out.println(order);
        try {

            PaymentOrders paymentOrders = PaymentOrders.builder()
                    .payLoad(order)
                    .build();

            paymentOrdersRepository.save(paymentOrders);
        } catch (Exception e) {
            log.error("ОШИБКА ПАРСИНГА JSON!!!!!!!!!!!");
            throw new RuntimeException(e.getMessage());
        }


    }

}
