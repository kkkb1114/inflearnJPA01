package com.example.inflearnJPA01.service.query;

import com.example.inflearnJPA01.domain.OrderItem;
import lombok.Getter;

@Getter
class OrderItemDto {
    private String ItemName;
    private int ItemPrice;
    private int count;

    public OrderItemDto(OrderItem orderItem) {
        this.ItemName = orderItem.getItem().getName();
        this.ItemPrice = orderItem.getItem().getPrice();
        this.count = orderItem.getItem().getStockQuantity();
    }
}
