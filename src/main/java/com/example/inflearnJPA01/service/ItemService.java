package com.example.inflearnJPA01.service;

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
}
