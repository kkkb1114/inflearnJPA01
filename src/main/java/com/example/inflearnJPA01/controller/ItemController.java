package com.example.inflearnJPA01.controller;

import com.example.inflearnJPA01.domain.item.Book;
import com.example.inflearnJPA01.domain.item.Item;
import com.example.inflearnJPA01.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("bookForm", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(@Valid BookForm bookForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "items/createItemForm";
        }else {
            Book book = Book.createBook(bookForm.getId(), bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity(), bookForm.getAuthor(), bookForm.getIsbn());
            itemService.ItemJoin(book);
            return "redirect:/items";
        }
    }

    @GetMapping("/items")
    public String findAll(Model model){
        List<Item> itemList = itemService.ItemFindAll();
        model.addAttribute("itemList", itemList);
        return "items/itemList";
    }

    @GetMapping("/items/{id}/edit")
    public String modifiedForm(@PathVariable Long id, Model model){
        Book book = (Book) itemService.ItemFindOne(id);
        BookForm bookForm = new BookForm();
        bookForm.setId(book.getId());
        bookForm.setName(book.getName());
        bookForm.setPrice(book.getPrice());
        bookForm.setStockQuantity(book.getStockQuantity());
        bookForm.setAuthor(book.getAuthor());
        bookForm.setIsbn(book.getIsbn());

        model.addAttribute("item", bookForm);
        return "items/updateItemForm";
    }

    /**
     * @ModelAttribute: updateItemForm.html에서 post로 BookForm을 날렸기 때문에 해당 어노테이션으로 받을 수도 있다.
     */
    @PostMapping("/items/{id}/edit")
    public String modified(@ModelAttribute BookForm bookForm){
        Item book = Book.createBook(bookForm.getId(), bookForm.getName(), bookForm.getPrice(), bookForm.getStockQuantity(), bookForm.getAuthor(), bookForm.getIsbn());
        itemService.ItemJoin(book);
        return "redirect:/items";
    }
}
