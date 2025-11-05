package by.antohakon.paymentservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record OrderEvent(

	@JsonProperty("quantity")
	int quantity,

	@JsonProperty("orderId")
	Long orderId,

	@JsonProperty("orderName")
	String orderName
) {
}