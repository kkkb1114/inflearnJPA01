package com.example.inflearnJPA01.api;

import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.repository.OrderRepository;
import com.example.inflearnJPA01.repository.OrderSearch;
import com.example.inflearnJPA01.repository.order_repository.OrderSimpleQueryDto;
import com.example.inflearnJPA01.repository.order_repository.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)의 성능 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 **/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> orderList = orderRepository.orderFindAll(new OrderSearch());
        return orderList;
    }

    @GetMapping("/api/v2/simple-orders")
    public Result<List<OrderSimpleQueryDto>> orderV2() {
        // 이렇게 로직을 짜면 DTO에 담아 엔티티를 노출시키지 않지만 치명적인 단점이 있다.
        // Order가 현재 2개라 2개가 조회될텐데 그럼 Order안에 있는 Member와 Delivery가 한번씩 select 조회가 되어 아래와 같이 select문이 나간다.
        // 1 + N -> 1(Order) + N(Member) + N(Delivery): Order가 2개이므로 N이 4번이다. 총 5번 select 문이 날라간다.
        /**해결법: fetchJoin을 이용하면 된다.**/
        List<Order> orderList = orderRepository.orderFindAll(new OrderSearch());
        List<OrderSimpleQueryDto> collect = orderList.stream()
                .map(o -> new OrderSimpleQueryDto(
                        o.getId(),
                        o.getMember().getName(),
                        o.getOrderDate(),
                        o.getOrderStatus(),
                        o.getDelivery().getAddress()
                ))
                .collect(Collectors.toList());
        return new Result<>(collect.size(), collect);
    }

    @GetMapping("/api/v3/simple-orders")
    public Result<List<OrderSimpleQueryDto>> orderV3() {
        // findAllWithMemberDelivery()를 통해 join fetch을 사용했다.(join fetch에 대해 100% 무조건 이해해야한다고 한다. (by김영한))
        List<Order> orderList = orderRepository.findAllWithMemberDelivery();
        List<OrderSimpleQueryDto> collector = orderList.stream()
                .map(o -> new OrderSimpleQueryDto(
                        o.getId(),
                        o.getMember().getName(),
                        o.getOrderDate(),
                        o.getOrderStatus(),
                        o.getDelivery().getAddress()
                ))
                .collect(Collectors.toList());
        return new Result<>(collector.size(), collector);
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

    @Data
    @AllArgsConstructor
    class Result<T> {
        private int count;
        private T data;
    }
}
