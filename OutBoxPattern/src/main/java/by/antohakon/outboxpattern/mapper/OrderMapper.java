package by.antohakon.outboxpattern.mapper;

import by.antohakon.outboxpattern.dto.CreatedOrderDto;
import by.antohakon.outboxpattern.dto.OrderDto;
import by.antohakon.outboxpattern.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    //@Mapping(source = "orderId", target = "orderId")
    OrderDto toDto(Order order);

    @Mapping(source = "orderName", target = "orderName")
    Order toEntity(CreatedOrderDto orderDto);

}
