package com.example.inflearnJPA01.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @Embedded: 내장 타입 어노테이션을 포함한 클래스라는 뜻이다. (Address.class를 보면 @Embeddable를 지정하였다.)
 * @setter: @setter는 실무에선 Entit에 사용하지 않는다.
 */
@Entity
@Getter @Setter
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orderList = new ArrayList<>();
}
