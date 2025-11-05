package by.antohakon.paymentservice.repository;

import by.antohakon.paymentservice.entity.PaymentOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentOrdersRepository extends JpaRepository<PaymentOrders, Long> {

    List<PaymentOrders> findByPaymentStatusFalse();

    PaymentOrders findByOrderId(Long orderId);

    @Query("""
            update PaymentOrders p
            set p.paymentStatus = true
            where p.orderId between 90 and 180
            """)
    @Modifying
    void setStatusPay();

}
