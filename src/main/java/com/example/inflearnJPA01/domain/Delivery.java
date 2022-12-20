package com.example.inflearnJPA01.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    // EnumType중 가능한 String으로 지정하자. ordinal은 숫자(순서)로 나오기에 중간에 다른 데이터가 끼면 그 뒤에 있는 데이터의 ordinal 값은 바뀌기에 좋지 않다.
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
}
