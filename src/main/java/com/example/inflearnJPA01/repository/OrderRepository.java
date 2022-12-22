package com.example.inflearnJPA01.repository;

import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    /**
     * 주문
     */
    public void save(Order order) {
        entityManager.persist(order);
    }

    /**
     * 주문 취소
     */
    public void orderCancel(Order order) {
        entityManager.remove(order);
    }

    /**
     * 주문 조회
     */
    // 해당 기능처럼 복잡한 기능은 순수 JPA 기능으로는 유지보수 측면에서 무리가 있다고 한다.
    // 때문에 Querydsl을 사용하는데 해당 강의는 따로 결제해야할듯 하다.
    public List<Order> orderFindAll(OrderSearch orderSearch) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }
        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = entityManager.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();
    }

    public Order orderFindOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    //todo join fetch에 대해 100% 무조건 이해해야한다고 한다. (by김영한)
    public List<Order> findAllWithMemberDelivery() {
        return entityManager.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithOrderItem() {
        return entityManager.createQuery("select distinct o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d" +
                " join fetch o.orderItemList oi" +
                " join fetch oi.item i", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return entityManager.createQuery("select o from Order o" +
                " join fetch o.member m" +
                " join fetch o.delivery d", Order.class
        ).setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
