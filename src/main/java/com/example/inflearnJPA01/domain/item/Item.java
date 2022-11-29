package com.example.inflearnJPA01.domain.item;

import com.example.inflearnJPA01.domain.Category;
import com.example.inflearnJPA01.domain.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * abstract: 추상 클래스
 * 원래대로라면 Entity클래스는 @setter를 넣는 것이 아닌 값을 변경할 일이 있으면 아래 비즈니스 로직과 같이 메소드를 통해 변경해야한다.
 */
@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String nama; // 상품 이름
    private int price; // 상품 가격
    private int stockQuantity; // 상품 재고 수량
    @ManyToMany(mappedBy = "itemList")
    private List<Category> categoryList = new ArrayList<>(); // 카테고리 리스트

    //==비즈니스 로직==//
    /**
     * 재고(stock) 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 재고(stock) 감소 (재고가 0과 같거나 적으면 Exception 발생)
     */
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
