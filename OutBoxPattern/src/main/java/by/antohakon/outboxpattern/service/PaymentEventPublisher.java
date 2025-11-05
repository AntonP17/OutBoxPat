package by.antohakon.outboxpattern.service;

import by.antohakon.outboxpattern.entity.OutBox;
import by.antohakon.outboxpattern.repository.OutboxRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentEventPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void publishEvents(){

        List<OutBox> outboxList = outboxRepository.findAll();

        for(OutBox outbox : outboxList){
            System.out.println(outbox);

            try {
                kafkaTemplate.send("payment-topic", outbox.getPayload());

                outboxRepository.delete(outbox);
            } catch (Exception e) {
                log.error("ОШИБКАААААА " + e.getMessage());
            }
        }

    }

}
