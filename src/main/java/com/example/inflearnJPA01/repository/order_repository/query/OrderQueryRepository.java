package com.example.inflearnJPA01.repository.order_repository.query;

import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.domain.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager entityManager;

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(o -> {
            List<OrderItemQueryDto> findOrderItems = findOrderItems(o.getOrderId());
            o.setOrderItemQueryDtoList(findOrderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return entityManager.createQuery(
                "select new com.example.inflearnJPA01.repository.order_repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    // 이렇게 jpql을 짜더라도 컬렉션을 바로 넣을수는 없기에 일대다 연관관계인 orderItemList를 뺐다.
    public List<OrderQueryDto> findOrders() {
        return entityManager.createQuery
                ("select new com.example.inflearnJPA01.repository.order_repository.query.OrderQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * < findOrderQueryDtos()과 차이점 >
     * 1. findOrderQueryDtos()은 OrderItemQueryDto를 forEach()를 통해 OrderQueryDto개수만큼 select문을 돌렸다.
     * 2. findAllByDto_optimization()은 OrderItemQueryDto은 쿼리문에 orderIdList를 넣어 orderItemQueryDtoList 객체를 만든 다음
     *    orderItemQueryDtoList를 Map으로 변환 시킨 후 OrderItemQueryDto에 set 시켜 한번에 조회가 가능해졌다.
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> orderQueryDtoList = findOrders();

        List<Long> orderIdList = orderQueryDtoList.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        List<OrderItemQueryDto> orderItemQueryDtoList = entityManager.createQuery(
                "select new com.example.inflearnJPA01.repository.order_repository.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIdList)
                .getResultList();


        // 람다 문법으로 List를 Map으로 변환 (키: OrderItemQueryDto::getOrderId, 값: orderItemQueryDtoList)
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItemQueryDtoList.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        orderQueryDtoList.forEach(o -> o.setOrderItemQueryDtoList(orderItemMap.get(o.getOrderId())));

        return orderQueryDtoList;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return entityManager.createQuery(
                "select new com.example.inflearnJPA01.repository.order_repository.query.OrderFlatDto(o.id, m.name, o.orderDate, o.orderStatus, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItemList oi" +
                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
