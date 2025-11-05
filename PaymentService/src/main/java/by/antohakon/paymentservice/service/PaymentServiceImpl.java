package by.antohakon.paymentservice.service;

import by.antohakon.paymentservice.entity.PaymentOrders;
import by.antohakon.paymentservice.repository.PaymentOrdersRepository;
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
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrdersRepository paymentOrdersRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    //@Scheduled(fixedRate = 5000)
    @Override
    @Transactional
    public void processPaymentSheduled() {

        List<PaymentOrders> paymentOrders = paymentOrdersRepository.findByPaymentStatusFalse();

        for (PaymentOrders paymentOrder : paymentOrders) {
            // Четные ID - успех, нечетные - неудача
            boolean isSuccess = paymentOrder.getOrderId() % 2 == 0;

            if (isSuccess) {
                paymentOrder.setPaymentStatus(true);
                paymentOrdersRepository.save(paymentOrder);
                kafkaTemplate.send("sucessfull-payment-topic",
                        "SUCCESS: " + paymentOrder.getOrderId());
            } else {
                kafkaTemplate.send("exceptions",
                        "FAILED: " + paymentOrder.getOrderId());
                deleteByOrderId(paymentOrder.getOrderId());
            }
        }
    }

    public void processPayment(PaymentOrders paymentOrders) {

        // Четные ID - успех, нечетные - неудача
        boolean isSuccess = paymentOrders.getOrderId() % 2 == 0;

        if (isSuccess) {
            paymentOrders.setPaymentStatus(true);
            paymentOrdersRepository.save(paymentOrders);
            kafkaTemplate.send("sucessfull-payment-topic",
                    "SUCCESS: " + paymentOrders.getOrderId());
        } else {
            kafkaTemplate.send("exceptions",
                    "FAILED: " + paymentOrders.getOrderId());
           // deleteByOrderId(paymentOrders.getOrderId());
        }

    }

    private void deleteByOrderId(Long orderId) {

        PaymentOrders findOrders = paymentOrdersRepository.findByOrderId(orderId);
        if (findOrders == null) {
            log.error("Order not found");
        }

        paymentOrdersRepository.delete(findOrders);

    }

}
