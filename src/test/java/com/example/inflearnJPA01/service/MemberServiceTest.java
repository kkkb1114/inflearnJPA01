package com.example.inflearnJPA01.service;

import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // JUnit을 실행할때 스프링이랑 같이 엮어서 실행하기 위한 것
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager entityManager;

    // 해당 테스트는 @Rollback 어노테이션을 false로 설정하거나 entityManager.flush() 이 처럼 entityManager로 flush()하여 영속성 컨텍스트에 있는 sql문을 실행시키지 않으면
    // @rollback 설정으로 인해 데이터를 db에 넣지는 않는다. (테스트에서 rollback을 하는 이유는 테스트는 반복해서 테스트하기에 데이터가 저장되지 않는다는 것을 기본 전제로 하는 것이다.)
    @Test
    @Rollback(value = false)
    void memberJoin() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("김기범");

        //when
        Long id = memberService.memberJoin(member1);

        //then
        entityManager.flush(); // 영속성 컨텍스트에 있는 sql문을 실행
        Assertions.assertThat(memberRepository.findOne(id)).isEqualTo(member1);
    }

    @Test
    public void memberValidate(){
        //given
        Member member1 = new Member();
        member1.setName("김기범1");

        Member member2 = new Member();
        member2.setName("김기범1");

        //when
        memberService.memberJoin(member1);
        try{
            memberService.memberJoin(member2); // 이미 있는 회원 이름으로 throw new IllegalStateException("이미 존재하는 회원입니다.");에서 걸려서 에러가 뜬다.
        } catch (IllegalStateException e) {
            System.out.println("중복 회원 테스트에서 걸림");
            e.printStackTrace();
        }


        //then
        Assertions.fail("예외가 발생해야하기에 해당 에러가 뜨면 안된다.");
    }

    @Test
    void memberFindAll() throws Exception{
        //given

        //when

        //then

    }

    @Test
    void memberFindOne() throws Exception{
        //given

        //when

        //then

    }
}