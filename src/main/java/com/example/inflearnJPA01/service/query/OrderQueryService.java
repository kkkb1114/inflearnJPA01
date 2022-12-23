package com.example.inflearnJPA01.service.query;

import com.example.inflearnJPA01.api.OrderApiController;
import com.example.inflearnJPA01.domain.Address;
import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.domain.OrderStatus;
import com.example.inflearnJPA01.repository.OrderRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public List<OrderDto> orderListV3() {
        List<Order> orderList = orderRepository.findAllWithOrderItem();
        return orderList.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }


}
