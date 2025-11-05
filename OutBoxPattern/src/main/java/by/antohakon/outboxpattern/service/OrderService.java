package by.antohakon.outboxpattern.service;

import by.antohakon.outboxpattern.dto.CreatedOrderDto;
import by.antohakon.outboxpattern.dto.OrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderDto> getAllOrders(Pageable pageable);
    OrderDto getOrderById(Long orderId);
    OrderDto createOrder(CreatedOrderDto createdOrderDto);
    OrderDto updateOrder(Long orderId, CreatedOrderDto createdOrderDto);
    void deleteOrder(Long id);

}
