package com.example.inflearnJPA01;

import com.example.inflearnJPA01.domain.*;
import com.example.inflearnJPA01.domain.item.Book;
import com.example.inflearnJPA01.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * userA
 *  JPA1 BOOK
 *  JPA2 BOOK
 * userB
 *  SPRING1 BOOK
 *  SPRING2 BOOK
 * **/
@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitDbService initDbService;

    // 해당 클래스 생성시 바로 동작
    @PostConstruct
    public void init(){
        initDbService.dbInit1();
        initDbService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitDbService{

        private final EntityManager entityManager;

        public void dbInit1(){

            Member member = createMember("김기범", "도시1", "거리1", "번지1");
            entityManager.persist(member);

            Book JPA_book = createBook(0L, "JPA1 BOOK", 1000, 10, "작가1", "11");
            Book JPA_book2 = createBook(0L, "JPA2 BOOK", 2000, 20, "작가2", "22");
            entityManager.persist(JPA_book);
            entityManager.persist(JPA_book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(JPA_book, 1000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(JPA_book2, 2000, 2);
            entityManager.persist(orderItem1);
            entityManager.persist(orderItem2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            entityManager.persist(orderItem2);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            entityManager.persist(order);
        }

        public void dbInit2(){

            Member member = createMember("김기범2", "도시2", "거리2", "번지2");
            entityManager.persist(member);

            Book SPRING_book = createBook(0L, "SPRING1 BOOK", 1000, 10, "작가1", "11");
            Book SPRING_book2 = createBook(0L, "SPRING2 BOOK", 2000, 20, "작가2", "22");
            entityManager.persist(SPRING_book);
            entityManager.persist(SPRING_book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(SPRING_book, 1000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(SPRING_book2, 2000, 2);
            entityManager.persist(orderItem1);
            entityManager.persist(orderItem2);

            Delivery delivery2 = createDelivery(member.getAddress());
            entityManager.persist(delivery2);

            Order order2 = Order.createOrder(member, delivery2, orderItem1, orderItem2);
            entityManager.persist(order2);
        }

        // 회원 객체 생성
        public Member createMember(String name, String city, String street, String zipcode){
            Member member1 = new Member();
            member1.setName(name);
            member1.setAddress(new Address(city, street, zipcode));
            return member1;
        }

        // 책 객체 생성
        public Book createBook(Long id, String name, int price, int stockQuantity, String author, String isbn){
            return Book.createBook(id, name, price, stockQuantity, author, isbn);
        }

        // 배달 객체 생성
        public Delivery createDelivery(Address address){
            Delivery delivery = new Delivery();
            delivery.setAddress(address);
            return delivery;
        }
    }
}
