package com.apteka24.controllers;

import com.apteka24.dto.CartItem;
import com.apteka24.dto.OrderRequest;
import com.apteka24.models.Order;
import com.apteka24.models.Product;
import com.apteka24.models.User;
import com.apteka24.services.OrderService;
import com.apteka24.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;
    @MockBean
    private UserService userService;

    @Test
    void createOrder_success_whenUserExists() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setUserId(1L);
        Product p = new Product();
        p.setPrice(new BigDecimal("5.00"));
        CartItem item = new CartItem();
        item.setProduct(p);
        item.setQuantity(2);
        req.setCartItems(List.of(item));
        when(userService.getUserById(1L)).thenReturn(Optional.of(new User()));
        Order saved = new Order();
        saved.setId(10L);
        when(orderService.createOrder(any(Order.class), any())).thenReturn(saved);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    void createOrder_badRequest_whenUserMissing() throws Exception {
        OrderRequest req = new OrderRequest();
        req.setUserId(999L);
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listAndGetAndUpdate() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(new Order()));
        when(orderService.getUserOrders(1L)).thenReturn(List.of(new Order()));
        Order found = new Order();
        found.setId(5L);
        when(orderService.getOrderById(5L)).thenReturn(Optional.of(found));
        when(orderService.getOrderById(6L)).thenReturn(Optional.empty());
        Order updated = new Order();
        updated.setId(5L);
        updated.setStatus("DONE");
        when(orderService.updateOrderStatus(org.mockito.ArgumentMatchers.eq(5L), any(String.class))).thenReturn(updated);

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/orders/user/1"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/orders/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
        mockMvc.perform(get("/api/orders/6"))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/orders/5/status")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("DONE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }
}


