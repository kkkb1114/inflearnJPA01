package com.example.inflearnJPA01.repository.order_repository.query;

import com.example.inflearnJPA01.domain.Address;
import com.example.inflearnJPA01.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class OrderFlatDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private String itemName;
    private int orderPrice;
    private int count;
}
