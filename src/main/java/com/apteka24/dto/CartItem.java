package com.apteka24.dto;

import com.apteka24.models.Product;

public class CartItem {
    private Product product;
    private Integer quantity;

    // Конструкторы
    public CartItem() {}

    public CartItem(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Геттеры и сеттеры
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}