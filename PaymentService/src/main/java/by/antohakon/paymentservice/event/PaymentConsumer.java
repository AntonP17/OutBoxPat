package by.antohakon.paymentservice.event;

import by.antohakon.paymentservice.dto.OrderEvent;
import by.antohakon.paymentservice.entity.PaymentOrders;
import by.antohakon.paymentservice.repository.PaymentOrdersRepository;
import by.antohakon.paymentservice.service.PaymentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PaymentOrdersRepository paymentOrdersRepository;
    private final PaymentServiceImpl paymentService;

    @KafkaListener(
            topics = "${kafka.topic.three}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    @Transactional
    public void listenOrder(String order) {

        System.out.println(order);
        try {

            OrderEvent orderEvent = objectMapper.readValue(order, OrderEvent.class);
            log.info("После парсинга - " + orderEvent.toString());

            PaymentOrders paymentOrders = PaymentOrders.builder()
                    .orderId(orderEvent.orderId())
                    .orderName(orderEvent.orderName())
                    .quantity(orderEvent.quantity())
                    .build();

            paymentOrdersRepository.save(paymentOrders);

            paymentService.processPayment(paymentOrders);
        } catch (Exception e) {
            log.error("ОШИБКА ПАРСИНГА JSON!!!!!!!!!!!");
            throw new RuntimeException(e.getMessage());
        }


    }

}
