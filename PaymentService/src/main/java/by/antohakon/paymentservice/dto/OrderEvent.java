package by.antohakon.paymentservice.dto;

import lombok.Builder;

@Builder
public record OrderEvent(Long orderId, String orderName,  int quantity) {
}
