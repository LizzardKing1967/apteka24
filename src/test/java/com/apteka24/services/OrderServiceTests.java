package com.apteka24.services;

import com.apteka24.dto.CartItem;
import com.apteka24.models.Order;
import com.apteka24.models.OrderItem;
import com.apteka24.models.Product;
import com.apteka24.models.User;
import com.apteka24.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_calculatesTotalAndSaves() {
        Order order = new Order();
        order.setUser(new User());
        Product p1 = new Product();
        p1.setPrice(new BigDecimal("10.00"));
        Product p2 = new Product();
        p2.setPrice(new BigDecimal("2.50"));

        CartItem c1 = new CartItem();
        c1.setProduct(p1);
        c1.setQuantity(2);
        CartItem c2 = new CartItem();
        c2.setProduct(p2);
        c2.setQuantity(3);

        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        Order saved = orderService.createOrder(order, List.of(c1, c2));

        assertThat(saved.getItems()).extracting(OrderItem::getQuantity).containsExactly(2, 3);
        assertThat(saved.getTotalAmount()).isEqualByComparingTo(new BigDecimal("27.50"));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getUserOrders_returnsByUserWhenExists() {
        User user = new User();
        user.setId(99L);
        when(userService.getUserById(99L)).thenReturn(Optional.of(user));
        when(orderRepository.findByUserOrderByOrderDateDesc(user)).thenReturn(List.of(new Order()));

        List<Order> orders = orderService.getUserOrders(99L);

        assertThat(orders).hasSize(1);
    }

    @Test
    void getUserOrders_returnsEmptyWhenUserMissing() {
        when(userService.getUserById(7L)).thenReturn(Optional.empty());

        List<Order> orders = orderService.getUserOrders(7L);

        assertThat(orders).isEmpty();
    }

    @Test
    void getAllOrders_sorted() {
        when(orderRepository.findAllByOrderByOrderDateDesc()).thenReturn(List.of(new Order(), new Order()));

        List<Order> orders = orderService.getAllOrders();

        assertThat(orders).hasSize(2);
        verify(orderRepository).findAllByOrderByOrderDateDesc();
    }

    @Test
    void getOrderById_delegates() {
        when(orderRepository.findById(5L)).thenReturn(Optional.of(new Order()));

        Optional<Order> o = orderService.getOrderById(5L);

        assertThat(o).isPresent();
    }

    @Test
    void updateOrderStatus_updatesWhenFound() {
        Order order = new Order();
        order.setStatus("NEW");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updated = orderService.updateOrderStatus(1L, "DONE");

        assertThat(updated.getStatus()).isEqualTo("DONE");
    }

    @Test
    void updateOrderStatus_returnsNullWhenMissing() {
        when(orderRepository.findById(123L)).thenReturn(Optional.empty());

        Order updated = orderService.updateOrderStatus(123L, "DONE");

        assertThat(updated).isNull();
    }
}


