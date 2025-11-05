package by.antohakon.paymentservice.repository;

import by.antohakon.paymentservice.entity.PaymentOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentOrdersRepository extends JpaRepository<PaymentOrders, Long> {
}
