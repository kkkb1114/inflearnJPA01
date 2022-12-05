package com.example.inflearnJPA01.service;

import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.domain.item.Book;
import com.example.inflearnJPA01.domain.item.Item;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class ItemUpdateTest {
    @Autowired
    private EntityManager entityManager;

    /**
     * <변경 감지 법>
     * 1. 해당 메서드는 JPA의 변경 감지 기능을 테스트한 메서드다.
     * 2. JPA는 DB에 없는 데이터를 저장하는 경우 persist를 해야만 저장이 되지만 이미 있는 데이터의 경우 중간에 데이터만 변경해줘도 JPA가
     *    알아서 UPDATE 쿼리를 저장하여 컨텍스트가 commit할때 데이터를 UPDATE한다.
     * 3. 이를 dirty check라고 한다.
     * 4. 또한 이미 DB에 있다가 나와서 기존 식별자를 가지고 있으면 데이터는 준영속성 엔티티라고 한다.
     */
    @Test
    @Rollback(value = false)
    public void updateTest() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("김기범");

        //when
        entityManager.persist(member1); // 최초 DB 저장
        Member member = entityManager.find(Member.class, 1L);
        System.out.println("updateTest: "+member.getName());
        member.setName("김기범23232"); // dirty check
        Member member2 = entityManager.find(Member.class, 1L);
        System.out.println("updateTest: "+member2.getName()); // persist를 하지 않았는데 db에서 다시 꺼네보면 데이터가 바뀌어있다.

        //then
    }
}
