package com.example.inflearnJPA01.service;

import com.example.inflearnJPA01.domain.*;
import com.example.inflearnJPA01.domain.item.Item;
import com.example.inflearnJPA01.repository.ItemRepository;
import com.example.inflearnJPA01.repository.MemberRepository;
import com.example.inflearnJPA01.repository.OrderRepository;
import com.example.inflearnJPA01.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    public Long save(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOneId(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        
        return order.getId();
    }

    /**
     * 주문 취소
     */
    public void orderCancel(Long orderId){
        Order order = orderRepository.orderFindOne(orderId);
        order.cancle();
    }

    /**
     * 주문 검색
     */
    @Transactional(readOnly = true)
    public List<Order> orderSearch(OrderSearch orderSearch){
        List<Order> orderList = orderRepository.orderFindAll(orderSearch);
        return orderList;
    }
}
