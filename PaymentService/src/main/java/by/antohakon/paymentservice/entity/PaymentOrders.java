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

    private Long orderId;

    private String orderName;

    private int quantity;

    private boolean paymentStatus = false;

}
