package by.antohakon.outboxpattern.service;

import by.antohakon.outboxpattern.entity.OutBox;
import by.antohakon.outboxpattern.repository.OutboxRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Transactional
    @Scheduled(fixedRate = 5000)
    public void publishEvents() {

        List<OutBox> outboxList = outboxRepository.findAll();

        for (OutBox outbox : outboxList) {
            try {
                // Circuit Breaker защищает от падения Kafka брокера
                sendToKafkaWithCircuitBreaker(outbox);

            } catch (Exception ex) {
                log.error("Failed to send message {}: {}", outbox, ex.getMessage());
                // Сообщение останется в Outbox для следующей попытки
            }

        }
    }

    @SneakyThrows
    @CircuitBreaker(name = "kafka", fallbackMethod = "kafkaFallback")
    public void sendToKafkaWithCircuitBreaker(OutBox outbox) {
        // Синхронная отправка с ожиданием подтверждения от Kafka брокера
        SendResult<String, String> result = kafkaTemplate
                .send("payment-topic", outbox.getPayload())
                .get(10, TimeUnit.SECONDS); // Ждем подтверждения 10 секунд

        // Если дошли сюда - Kafka брокер принял сообщение
        log.info(" Message {} sent to Kafka", outbox);
        outboxRepository.delete(outbox);
    }

    // Fallback вызывается когда Kafka брокер недоступен
    public void kafkaFallback(OutBox outbox, Throwable t) {
        log.warn("Kafka unavailable, message {} will retry later", outbox);
    }

}
