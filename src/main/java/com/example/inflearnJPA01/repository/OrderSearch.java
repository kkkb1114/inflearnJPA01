package com.example.inflearnJPA01.repository;

import com.example.inflearnJPA01.domain.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.EntityManager;

@Getter @Setter
@RequiredArgsConstructor
public class OrderSearch {
    private final EntityManager entityManager;
    private String memberName;
    private OrderStatus orderStatus;
}
