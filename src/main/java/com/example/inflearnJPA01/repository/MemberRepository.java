package com.example.inflearnJPA01.repository;

import com.example.inflearnJPA01.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * 1. @Repository: 해당 클래스를 스프링이 Repository 클래스로 인식 및 스프링 빈에 등록시킨다.
 * 2. @PersistenceContext: 해당 어노테이션이 붙은 EntityManager 관련 변수는 스프링 부트에서 영속성 컨텍스트에서 자동으로 객체 생성해서 내려준다.
 * 3. 여기에 사용되는 SQL문은 전부 스프링 영속성 컨텍스트에 저장되어있다가 빌드 및 사용되면 실행된다.
 */
@Repository
@RequiredArgsConstructor
public class MemberRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    public void save(Member member){
        entityManager.persist(member);
    }

    public Member findOne(Long id){
        return entityManager.find(Member.class, id);
    }

    // JPQL 사용
    public List<Member> findAll(){
        return entityManager.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // JPQL 사용
    public List<Member> findOne(String name){
        return entityManager.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
