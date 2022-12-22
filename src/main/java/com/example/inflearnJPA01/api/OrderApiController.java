package com.example.inflearnJPA01.api;

import com.example.inflearnJPA01.domain.*;
import com.example.inflearnJPA01.repository.OrderRepository;
import com.example.inflearnJPA01.repository.OrderSearch;
import com.example.inflearnJPA01.repository.order_repository.query.OrderFlatDto;
import com.example.inflearnJPA01.repository.order_repository.query.OrderQueryDto;
import com.example.inflearnJPA01.repository.order_repository.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> orderListV1() {
        List<Order> orderList = orderRepository.orderFindAll(new OrderSearch());
        for (Order order : orderList) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItemList = order.getOrderItemList();
            orderItemList.stream().forEach(o -> o.getItem().getName());
        }
        return orderList;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderListV2() {
        List<Order> orderList = orderRepository.orderFindAll(new OrderSearch());
        return orderList.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }

    /**
     * 아래 클래스의 생성자와 같이 무조건 Entity클래스는 절대 노출되서는 안되기 때문에 Dto로 바꾼다. (OrderDto, OrderItemDto)
     **/
    @Getter
    class OrderDto {
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
                    .map(oi -> new OrderItemDto(oi))
                    .collect(Collectors.toList());
        }
    }

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

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderListV3() {
        List<Order> orderList = orderRepository.findAllWithOrderItem();
        return orderList.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v3-1/orders")
    public List<OrderDto> orderListV3_1(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                        @RequestParam(value = "limit", defaultValue = "100") int limit) {
        // ToOne 연관관계는 fetch join으로 전부 가져오기
        List<Order> orderList = orderRepository.findAllWithMemberDelivery(offset, limit);
        return orderList.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    // 해당 방법은 OrderQueryDto과 OrderItemQueryDto을 각각 jpql로 찾았으나 결국 Order는 한번 조회지만 그 안에 있는 N개인 OrderItem를 N번 조회하기때문에 완벽한 최적화는 아니다.
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderListV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> orderListV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> orderListV6() {
        return orderQueryRepository.findAllByDto_flat();
    }
}
