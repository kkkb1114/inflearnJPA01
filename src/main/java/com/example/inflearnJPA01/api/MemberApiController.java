package com.example.inflearnJPA01.api;

import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.dto.MemberDto;
import com.example.inflearnJPA01.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @RestController안에 있는 @ResponseBody는 데이터를 JsonBody로 바로 보내기 위한 설정이다.
 */
@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    //회원 조회
    /**
     * 1. 이렇게 Entity 클래스를 직접적으로 노출시키면 단점이 Entity클래스 스펙이 변경될경우 api자체 스펙이 변경되기 때문에
     *    api 자체의 장애가 날수 있다.
     * **/
    @GetMapping("/api/v1/members")
    public List<Member> findAllMemberV1(){
        return memberService.memberFindAll();
    }

    @GetMapping("/api/v2/members")
    public Result findAllMemberV2(){
        List<Member> memberList = memberService.memberFindAll();
        // List<Member> 객체를 List<MemberDto>으로 변환한 것이다.
        List<MemberDto> collect = memberList.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    /**
     * 1. 강의에선 이렇게 감싸주어야 유연성이 좋아진다고 한다. (<T>타입이라서)
     * 2. 그냥 결과값을 뱉으면 json 형식으로 굳혀지기때문에 별로라고 하는데 나는 아직 별로인 이유를 모르겠다.
     * **/
    @Data
    @AllArgsConstructor
    class Result<T>{
        private int count;
        private T data; // 제네럴 타입으로 어떤 타입이든 될수가 있다. (String이 될수도 int가 될수도 boolean이 될수도 있다.)
    }

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

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request){

        memberService.update(id, request.getName());
        Member member = memberService.memberFindOneId(id);
        return new UpdateMemberResponse(member.getId(), member.getName());
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }
}
