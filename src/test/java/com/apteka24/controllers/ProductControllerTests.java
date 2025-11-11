package com.apteka24.controllers;

import com.apteka24.models.Product;
import com.apteka24.services.ProductService;
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

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProducts_ok() throws Exception {
        Product p = new Product();
        p.setId(1L);
        p.setName("A");
        when(productService.getAllProducts()).thenReturn(List.of(p));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getCategories_ok() throws Exception {
        when(productService.getCategories()).thenReturn(List.of("A","B"));

        mockMvc.perform(get("/api/products/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("A"));
    }

    @Test
    void getByCategory_ok() throws Exception {
        when(productService.getProductsByCategory("Cat")).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/api/products/category/Cat"))
                .andExpect(status().isOk());
    }

    @Test
    void search_ok() throws Exception {
        when(productService.searchProducts("q")).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/api/products/search").param("q", "q"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_foundAndNotFound() throws Exception {
        Product p = new Product();
        p.setId(10L);
        when(productService.getProductById(10L)).thenReturn(Optional.of(p));
        when(productService.getProductById(11L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));

        mockMvc.perform(get("/api/products/11"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_update_delete() throws Exception {
        Product toCreate = new Product();
        toCreate.setName("New");
        toCreate.setPrice(new BigDecimal("1.23"));
        Product saved = new Product();
        saved.setId(5L);
        saved.setName("New");
        saved.setPrice(new BigDecimal("1.23"));
        when(productService.saveProduct(any(Product.class))).thenReturn(saved);
        when(productService.getProductById(5L)).thenReturn(Optional.of(saved));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));

        saved.setName("Changed");
        when(productService.saveProduct(any(Product.class))).thenReturn(saved);

        mockMvc.perform(put("/api/products/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Changed"));

        mockMvc.perform(delete("/api/products/5"))
                .andExpect(status().isOk());
    }
}


