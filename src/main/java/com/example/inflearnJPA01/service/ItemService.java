package com.example.inflearnJPA01.service;

import com.example.inflearnJPA01.domain.item.Book;
import com.example.inflearnJPA01.domain.item.Item;
import com.example.inflearnJPA01.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    public Long ItemJoin(Item item){
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional(readOnly = true)
    public List<Item> ItemFindAll(){
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item ItemFindOne(Long id){
        return itemRepository.findOneId(id);
    }

    @Transactional(readOnly = true)
    public List<Item> ItemFindName(String name){
        return itemRepository.findListName(name);
    }

    @Transactional
    public Item itemUpdate(Long itemId, Book book){
        Item item = ItemFindOne(itemId); // 영속상태의 Entity 객체
        item.itemUpdate(book.getId(), book.getName(), book.getPrice(), book.getStockQuantity());
        return item;
        // 해당 라인을 마지막으로 끝나면 @Transactional에 의해서 Transactional이 커밋이 된다.
        // 커밋이 되면 JPA는 flush를 날리고 스프링이 영속성 컨텍스트에 있는 Entity들을 전부 상태 확인하며 변경 사항이 있으면 db값을 수정한다.
    }
}
