package com.example.inflearnJPA01.domain;


import com.example.inflearnJPA01.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /**
     * 1. 다대다 연관관계로 실무에서는 사용 x(활용도에 유연함이 없고 실무에 써먹기 많이 좋지 않다. 예제를 위해 넣은 것 뿐)
     * 2. 다대다는 SQL상으로는 두 테이블을 묶어줄 용도의 테이블이 필요하며 @JoinTable 어노테이션으로 테이블을 만들고 각 두 테이블의 외래키 컬럼을 만들었다.
     */
    @ManyToMany
    @JoinTable(name = "category_table",
    joinColumns = @JoinColumn(name = "category_id"),
    inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> itemList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Category> child = new ArrayList<>();
}
