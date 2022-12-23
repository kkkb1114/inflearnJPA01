package com.example.inflearnJPA01.service.query;


import com.example.inflearnJPA01.domain.Address;
import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDto {
    private Long id;
    private String name; // 회원
    private Address address; // 배달 정보
    private LocalDateTime orderDate; // 주문 시간
    private OrderStatus orderStatus; // enum 클래스: 주문 상태 (ORDER, CAN CEL)
    private List<OrderItemDto> orderItemList = new ArrayList<>(); // 주문 상품 리스트

    public OrderDto(Order order) {
        this.id = order.getId();
        this.name = order.getMember().getName();
        this.address = order.getDelivery().getAddress();
        this.orderDate = order.getOrderDate();
        this.orderStatus = order.getOrderStatus();
        order.getOrderItemList().stream().forEach(o -> o.getItem().getName()); //프록시 초기화
        this.orderItemList = order.getOrderItemList().stream()
                .map(OrderItemDto::new)
                .collect(Collectors.toList());
    }
}
