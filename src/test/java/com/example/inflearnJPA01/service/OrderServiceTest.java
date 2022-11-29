package com.example.inflearnJPA01.service;

import com.example.inflearnJPA01.domain.Address;
import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.domain.OrderStatus;
import com.example.inflearnJPA01.domain.exception.NotEnoughStockException;
import com.example.inflearnJPA01.domain.item.Book;
import com.example.inflearnJPA01.domain.item.Item;
import com.example.inflearnJPA01.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Item book = createItem();

        //when
        Long orderId = orderService.save(member.getId(), book.getId(), 3);

        //then
        Order order = orderRepository.orderFindOne(orderId);
        Assertions.assertThat(order.getId()).isEqualTo(orderId); // 주문 Id값 검사
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER); // 주문 상태값 검사
        Assertions.assertThat(order.getTotalPrice()).isEqualTo(10000 * 3); // 주문 가격 검사
        Assertions.assertThat(book.getStockQuantity()).isEqualTo(7); // 상품 재고 검사
    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Item book = createItem();
        Long orderId = orderService.save(member.getId(), book.getId(), 10);

        //when
        orderService.orderCancel(orderId);
        //then
        Order order = orderRepository.orderFindOne(orderId);
        Assertions.assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
        Assertions.assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    @Test
    public void 재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item book = createItem();

        //when
        orderService.save(member.getId(), book.getId(), 11);

        //then
        fail("재고 수량 부족 예외가 발생해야함.");
    }

    private Member createMember(){
        Member member = new Member();
        member.setName("김기범1");
        member.setAddress(new Address("서울", "사당로17길", "현대아파트"));
        entityManager.persist(member);
        return member;
    }

    private Item createItem(){
        Item book = new Book();
        book.setNama("책");
        book.setPrice(10000);
        book.setStockQuantity(10);
        entityManager.persist(book);
        return book;
    }
}