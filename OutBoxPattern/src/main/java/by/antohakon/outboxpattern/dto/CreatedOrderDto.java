package by.antohakon.outboxpattern.dto;

import lombok.Builder;

@Builder
public record CreatedOrderDto(String orderName, int quantity) {
}
