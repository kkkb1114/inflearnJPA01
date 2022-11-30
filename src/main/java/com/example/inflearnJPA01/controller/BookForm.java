package com.example.inflearnJPA01.controller;

import com.example.inflearnJPA01.domain.Delivery;
import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.domain.OrderItem;
import com.example.inflearnJPA01.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class BookForm {
    private Long id;
    @NotEmpty(message = "상품 이름을 적어주세요.")
    private String name; // 상품 이름
    @NotNull
    @Range(min = 1000, max = 1000000)
    private int price; // 상품 가격
    private int stockQuantity; // 상품 재고 수량
    private String author; // 작가
    private String isbn;
}
