package by.antohakon.outboxpattern.service;

import by.antohakon.outboxpattern.entity.OutBox;
import by.antohakon.outboxpattern.repository.OutboxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        try {
            for (OutBox outbox : outboxList) {
                log.info("до отправки взли из БД - " + outbox);

                kafkaTemplate.send("payment-topic", outbox.getPayload())
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                log.info("сообщение отпарвлено в кафку - " + outbox);
                                outboxRepository.delete(outbox);
                            } else {
                                log.error("не удалось отпарвить сообщение в кафку - " + ex.getMessage());
                            }
                        });


            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}
