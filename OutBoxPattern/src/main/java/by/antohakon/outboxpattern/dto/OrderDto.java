package by.antohakon.outboxpattern.dto;

import lombok.Builder;

@Builder
public record OrderDto(Long orderId, String orderName, int quantity) {
}
