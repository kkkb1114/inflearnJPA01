package com.example.inflearnJPA01.repository;

import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * 주문
     */
    public void save(Order order){
        entityManager.persist(order);
    }

    /**
     * 주문 취소
     */
    public void orderCancel(Order order){
        entityManager.remove(order);
    }

    /**
     * 주문 조회
     */
    // 해당 기능처럼 복잡한 기능은 순수 JPA 기능으로는 유지보수 측면에서 무리가 있다고 한다.
    // 때문에 Querydsl을 사용하는데 해당 강의는 따로 결제해야할듯 하다.
    public List<Order> orderFindAll(OrderSearch orderSearch){
       return entityManager.createQuery("select o from Order o left join o.member m " +
               "where m.name like :name " +
               "and o.orderStatus :orderStatus", Order.class)
               .setParameter("name", orderSearch.getMemberName())
               .setParameter("orderStatus", orderSearch.getOrderStatus())
               .setFirstResult(100) // 100부터 시작
               .setMaxResults(1000) // 최대 1000건
               .getResultList();
    }

    public Order orderFindOne(Long id){
        return entityManager.find(Order.class, id);
    }
}
