package by.antohakon.paymentservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Table(name = "payment_orders")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PaymentOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentOrderId;

    private String payLoad;


}
