package com.example.inflearnJPA01.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 1. 연관관계는 무조건 지연로딩(LAZY)으로 설정해야한다.
 * 2. cascade = CascadeType.ALL의 CascadeType.ALL은 현재 entity 클래스 객체를 persist(저장)하면 그 안에 있는 모든 entity 클래스 데이터도 persist 해준다는 의미다.
 *    CascadeType.ALL은 무조건 상위 entity 클래스에만 사용해야하며 만약 무분별하게 사용시 게시글 <=> 댓글 관계에서 댓글을 지웠는데 게시글까지 지워질수도 있다.
 *    사용 예시: - 게시글 <=> 댓글 관계에서 게시글 안에 있는 연관관계 댓글 entity 클래스에 cascade = CascadeType.ALL를 거는 것은 OK
 *             - 게시글 <=> 댓글 관계에서 댓글 안에 있는 연관관계 게시글 entity 클래스에 cascade = CascadeType.ALL를 거는 것은 NO!!!!
 */
@Entity
@Getter @Setter
@Table(name = "orders") // 테이블 이름을 orders라고 지은 이유는 sql 자체에 order라는 명령어가 있어서 order라고 지으면 명령어로 인식하기에 앞에 s를 붙였다.
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 다른곳에서 내가 지정한 생성자 생성 방식 외에 기본 생성자 생성 방식 사용을 막기 위한 protected 기본 생성자 생성
public class Order {
    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 회원

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>(); // 주문 상품 리스트

    // 일대일 관계는 PK를 어디에 두든 상관 없지만 조회가 가장 많은 곳에 두는 곳이 좋으며 Delivery보단 Order의 조회가 더 많을 것으로 보여 Order에 두기로 했다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; // 배달 정보

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; // enum 클래스: 주문 상태 (ORDER, CAN CEL)

    // 연관관계 메서드 //
    public void setMember(Member member){
        this.member = member;
        member.getOrderList().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItem.setOrder(this);
        orderItemList.add(orderItem);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==//
    //이렇게 받아야 할 데이터가 많은 클래스일 경우 생성 메서드를 따로 만드는 것이 좋다. (이유는 앞으로 생성 시점을 변경 할일 있으면 해당 메서드만 변경하면 되기 때문이다.)
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancle(){
        // 배송상태가 완료 상태라면 주문 취소 불가
        if (delivery.getDeliveryStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능 합니다.");
        }

        this.setOrderStatus(OrderStatus.CANCEL);
        // 주문 하나가 취소되면 그 안에 있는 모든 주문 아이템은 취소가 되어야 하기에 전부 취소시킨다.
        for (OrderItem orderItem : orderItemList){
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItemList){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
