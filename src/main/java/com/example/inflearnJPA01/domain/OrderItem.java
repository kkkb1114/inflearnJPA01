package com.example.inflearnJPA01.domain;

import com.example.inflearnJPA01.domain.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item; // 상품

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; // 주문

    private int orderPrice; // 주문 가격

    private int count; // 주문 수량

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.item = item;
        orderItem.orderPrice = orderPrice;
        orderItem.count = count;

        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    /**
     * Order에서 주문 취소하여 상품 수량 주문 수량만큼 다시 증가
     */
    public void cancel(){
        getItem().addStock(count);
    }

    //==조회 로직==//
    /**
     * 주문 아이템 개수대로 가격 구함
     */
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}
