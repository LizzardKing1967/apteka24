package com.apteka24.controllers;

import com.apteka24.dto.OrderRequest;
import com.apteka24.models.Order;
import com.apteka24.services.OrderService;
import com.apteka24.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest orderRequest) {
        return userService.getUserById(orderRequest.getUserId())
                .map(user -> {
                    Order order = new Order();
                    order.setUser(user);
                    order.setDeliveryAddress(orderRequest.getDeliveryAddress());
                    order.setDeliveryType(orderRequest.getDeliveryType());
                    order.setDeliveryDate(orderRequest.getDeliveryDate());
                    order.setDeliveryTime(orderRequest.getDeliveryTime());
                    order.setComment(orderRequest.getComment());

                    Order savedOrder = orderService.createOrder(order, orderRequest.getCartItems());
                    return ResponseEntity.ok(savedOrder);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Optional<Order> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestBody String status) {
        Order order = orderService.updateOrderStatus(id, status);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }
}