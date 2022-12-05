package com.example.inflearnJPA01.controller;

import com.example.inflearnJPA01.domain.Member;
import com.example.inflearnJPA01.domain.Order;
import com.example.inflearnJPA01.domain.item.Item;
import com.example.inflearnJPA01.repository.OrderSearch;
import com.example.inflearnJPA01.service.ItemService;
import com.example.inflearnJPA01.service.MemberService;
import com.example.inflearnJPA01.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String orderForm(Model model){
        List<Member> memberList = memberService.memberFindAll();
        List<Item> itemList = itemService.ItemFindAll();
        model.addAttribute("memberList", memberList);
        model.addAttribute("itemList", itemList);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count){
        orderService.save(memberId, itemId, count);
        return "redirect:/orderList";
    }

    @GetMapping("/orderList")
    public String orderListForm(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
        List<Order> orderList = orderService.orderSearch(orderSearch);
        model.addAttribute("orderList", orderList);
        return "order/orderList";
    }

    @PostMapping(value = "/orderList/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.orderCancel(orderId);
        return "redirect:/orders";
    }
}
