package by.antohakon.outboxpattern.service;

import by.antohakon.outboxpattern.dto.CreatedOrderDto;
import by.antohakon.outboxpattern.dto.OrderDto;
import by.antohakon.outboxpattern.entity.Order;
import by.antohakon.outboxpattern.entity.OutBox;
import by.antohakon.outboxpattern.mapper.OrderMapper;
import by.antohakon.outboxpattern.repository.OrderRepository;
import by.antohakon.outboxpattern.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> OrderDto.builder()
                        .orderId(order.getOrderId())
                        .orderName(order.getOrderName())
                        .quantity(order.getQuantity())
                        .build());
    }

    @Override
    public OrderDto getOrderById(Long id) {
        log.info("getOrderById");
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            log.error("Order not found");
            throw new NoSuchElementException("Order not found");
        }

        return orderMapper.toDto(order);
    }

    @Override
    public OrderDto createOrder(CreatedOrderDto createdOrderDto) {

        log.info("createOrder");
        Order order = orderMapper.toEntity(createdOrderDto);
        orderRepository.save(order);

        registerEvent(order);

        return orderMapper.toDto(order);

    }

    private void registerEvent(Order order) {
        try {

            String payLoad = objectMapper.writeValueAsString(order);

            OutBox outBox = OutBox.builder()
                    .payload(payLoad)
                    .type("new order")
                    .createdAt(LocalDateTime.now())
                    .build();

            outboxRepository.save(outBox);

        } catch (JsonProcessingException e){
            log.error("Ошибка преобразования в json payload - " + order );
        }
    }

    @Override
    public OrderDto updateOrder(Long orderId, CreatedOrderDto createdOrderDto) {

        log.info("updateOrder");
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            log.error("Order not found");
            throw new NoSuchElementException("Order not found");
        }

        order.setOrderName(createdOrderDto.orderName());
        order.setQuantity(createdOrderDto.quantity());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public void deleteOrder(Long id) {
        log.info("deleteOrder");
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            log.error("Order not found");
            throw new NoSuchElementException("Order not found");
        }
        orderRepository.deleteById(id);
    }
}
