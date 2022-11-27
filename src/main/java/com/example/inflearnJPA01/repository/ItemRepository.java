package com.example.inflearnJPA01.repository;

import com.example.inflearnJPA01.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager entityManager;

    // 아래와 같이 id값을 체크한 이유는 처음 저장한 것이라면 id 값은 db에 저장될때 @GeneratedValue로 인하여 생성되지만
    // 현재 로직에 들어올땐 id값은 없는 것이 정상이며 만약 있다면 그것은 db에서 가져온것으로 간주하고 merge를 통해 update 하는 것이다.
    public void save(Item item){
        if (item.getId() == null){
            entityManager.persist(item);
        }else {
            entityManager.merge(item);
        }
    }

    public Item findOneId(Long id){
        return entityManager.find(Item.class, id);
    }

    public List<Item> findAll(){
        return entityManager.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

    public List<Item> findListName(String name){
        return entityManager.createQuery("select i from Item i where name = :name", Item.class)
                .setParameter("name", name)
                .getResultList();
    }
}
