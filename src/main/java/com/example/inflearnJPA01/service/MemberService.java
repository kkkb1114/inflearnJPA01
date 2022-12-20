package com.example.inflearnJPA01.service;

import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 1. @Transactional: JPA로 데이터 변경 동작시 @Transactional기반으로 동작하기에 무조건 추가해야한다.
 * 2. @Transactional은 기본 설정이 데이터 변경이기에 회원가입, 정보 수정같은 동작은 기본 설정으로 두지만 조회 같은 동작은 @Transactional(readOnly = true)로 설정하는 것이 좋다.
 *    (그래야 데이터를 읽는 것에 좀더 최적화 되어 동작한다.)
 * 3. @RequiredArgsConstructor: // 해당 어노테이션은 해당 클래스의 생성자를 자동으로 만들어주며 필드 내에 있는 final 변수만 자동으로 객체를 만들어준다.
 *                                 이러면 테스트에도 용이하며 실무에서 작성할때 해당 클래스는 MemberRepository를 의존하고 있다라는 것을 놓치지 않게 해준다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // 회원 가입
    public Long memberJoin(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    // 회원 중복 확인
    @Transactional(readOnly = true)
    private void validateDuplicateMember(Member member){
        List<Member> memberList = memberRepository.findOne(member.getName());
        //지울것!
        System.out.println("memberList.size:"+memberList.size());
        if (memberList.size() > 0){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    @Transactional(readOnly = true)
    public List<Member> memberFindAll(){
        return memberRepository.findAll();
    }

    // 회원 조건 조회
    @Transactional(readOnly = true)
    public List<Member> memberFindOneName(String name){
        return memberRepository.findOne(name);
    }

    @Transactional(readOnly = true)
    public Member memberFindOneId(Long id){
        return memberRepository.findOne(id);
    }

    // 회원 수정
    @Transactional
    public void update(Long id, String name){
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
