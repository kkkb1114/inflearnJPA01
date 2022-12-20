package com.example.inflearnJPA01.repository;

import com.example.inflearnJPA01.domain.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

@Getter @Setter
//@RequiredArgsConstructor
public class OrderSearch {
    @Autowired
    private EntityManager entityManager;
    private String memberName;
    private OrderStatus orderStatus;

    public OrderSearch(){

    }
}
