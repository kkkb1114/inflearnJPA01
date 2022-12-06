package com.example.inflearnJPA01.api;

import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @RestController안에 있는 @ResponseBody는 데이터를 JsonBody로 바로 보내기 위한 설정이다.
 */
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /**
     * - 단점1: 올바르지 않은 메서드 케이스로 이와 같이 Entity 클래스를 바로 받고 다루면
     *   {*Entity 클래스를 수정할 경우 해당 메서드에서는 알 수가 없어 알아차리지 못한 상태로 api 스펙이 달라져버린다.*}
     * - 단점1 요약: Entity 클래스 수정시 api 스펙이 바뀔수 있다.
     *
     * - 단점2: 사용자가 어떤 데이터를 날리는지 api 문서를 보지 않는 이상 알 수가 없다.
     *   {*Entity 클래스 안에는 여러 변수가 있으나 어떤 데이터든 여러 경우의 수로 받을 수 있기에 확인하지 않는 이상 알 수가 없다.*}
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.memberJoin(member);
        return new CreateMemberResponse(id);
    }

    /**
     * - 해당 메서드처럼 saveMemberV1()와 같이 Entity 클래스를 다이렉트로 받는 방식 보다 안전하다.
     * - 장점1: {*Entity 클래스를 변경해도 api가 받는 데이터는 변하지 않기 때문에 api에 영향을 주지 않는다.*}
     * - 장점1 요약: Entity 클래스 수정해도 받는 데이터는 바뀌지 않아 api 스펙이 바뀌지 않는다.
     *
     * - 장점2: CreateMemberRequest와 같은 클래스를 미리 만들어 놓고 바로 확인이 가능하여 {*어떤 데이터를 받는지 코드만 보고 명확하게 알 수가 있다.*}
     *
     * - 결론: CreateMemberRequest는 예시이며 보통 DTO와 같은 클래스를 따로 생성하여 데이터를 주고 받는다.
     * */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest createMemberRequest){
        Member member = new Member();
        member.setName(createMemberRequest.getName());
        Long id = memberService.memberJoin(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        protected CreateMemberResponse(){
        }
        public CreateMemberResponse(Long id){
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest{
        private String name;

        protected CreateMemberRequest(){
        }
        public CreateMemberRequest(String name){
            this.name = name;
        }
    }
}
