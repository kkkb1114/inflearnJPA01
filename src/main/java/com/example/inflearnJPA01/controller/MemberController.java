package com.example.inflearnJPA01.controller;

import com.example.inflearnJPA01.domain.Address;
import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

/**
 * 비즈니스 로직과 화면 로직은 무조건 별개로 가야한다.
 * 서로 섞이는 순간 정말 답이 없어진다.
 * 떄문에
 */
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    /**
     * @Valid memberForm : thymeleaf를 통해 해당 클래스 틀을 html파일에 넘겨 줄수가 있다.
     * bindingResult     : BindingResult는 스프링과 html을 강력하게 연결 시켜 BindingResult을 통해 스프링과 html 파일간 서로 데이터를 주고 받을수 있다.
     * 해당 메서드는 회원 가입 정보를 post로 보냈을때 결과를 BindingResult로 담아서 받으나 결과가 에러일 경우 해당 페이지에 MemberForm이 가지고 있는
     * @NotEmpty(message = "회원 이름은 필수 입니다.")를 name과 연결된 태그에 보내준다.
     * MemberForm        : Entity인 Member.class를 안쓰고 따로 폼을 만든 이유는 엔티티 클래스를 화면 처리와 엮으면 아주 간단한 경우에는 상관없지만 실무의 경우
     *                     대부분 화면 처리 로직과 엮이게 되며 Entity클래스에서 요구하는 변수 구성과 Form 클래스에서 요구하는 변수 구성이 다르게 되기 때문이다.
     * Entity            : Entity 클래스는 API를 통해 반환해서는 절대로 안된다.!!!!!
     */
    @PostMapping("/members/new")
    public String create(@Valid MemberForm memberForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "members/createMemberForm";
        } else {
            Address address = new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode());

            Member member = new Member();
            member.setName(memberForm.getName());
            member.setAddress(address);

            memberService.memberJoin(member);
            return "redirect:/";
        }
    }

    @GetMapping("/members")
    public String findAll(Model model) {
        // 원래는 Member를 그대로 뿌려주는 것이 아닌 정말 그 화면에만 필요한 DTO를 만들어서 보내야한다.
        List<Member> memberList = memberService.memberFindAll();
        model.addAttribute("memberList", memberList);
        return "members/memberList";
    }
}
