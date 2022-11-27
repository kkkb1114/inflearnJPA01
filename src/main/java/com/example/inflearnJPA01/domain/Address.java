package com.example.inflearnJPA01.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

/**
 * @Embeddable: JPA의 내장 타입이라는 뜻
 * 해당 타입은 데이터가 변경되면 안되기에 setter를 제공하지 않으며 무조건 생성자를 생성할때만 값을 넣도록 만든다.
 * (다른 값을 가진 Address가 필요하면 새로 만들어야한다는 뜻이다.)
 */
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    /**
     * 해당 생성자를 만든 이유는 JPA가 리플렉션이나 프록시 기술을 사용해야할때 기본 생성자가 없으면 해당 기술을 사용할수가 없다.
     * 때문에 리플렉션이나 프록시 기술 전용으로 기본 생성자를 만들며 다른곳에서 함부로 사용못하게 하기 위해 protected로 지정했다.
     */
    protected Address(){

    }

    public Address(String city, String street, String zipcode){
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
