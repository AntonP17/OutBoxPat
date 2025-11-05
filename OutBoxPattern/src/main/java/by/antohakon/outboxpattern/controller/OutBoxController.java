package by.antohakon.outboxpattern.controller;

import by.antohakon.outboxpattern.dto.CreatedOrderDto;
import by.antohakon.outboxpattern.dto.OrderDto;
import by.antohakon.outboxpattern.service.OrderService;
import by.antohakon.outboxpattern.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OutBoxController {

    private final OrderServiceImpl orderService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}")
    public OrderDto getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto createOrder(@RequestBody CreatedOrderDto orderDto) {
        return orderService.createOrder(orderDto);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{orderId}")
    public OrderDto updateOrder(@PathVariable Long orderId, @RequestBody CreatedOrderDto createdOrderDto) {
        return orderService.updateOrder(orderId, createdOrderDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
    }
}
