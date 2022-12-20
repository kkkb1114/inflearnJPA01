package com.example.inflearnJPA01.api;

import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.repository.OrderRepository;
import com.example.inflearnJPA01.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne(ManyToOne, OneToOne)의 성능 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 * **/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> orderList = orderRepository.orderFindAll(new OrderSearch());
        return orderList;
    }

}
