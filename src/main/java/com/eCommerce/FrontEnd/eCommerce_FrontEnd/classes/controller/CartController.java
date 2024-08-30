package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.CartRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Value("${eCommerce.backend}")
    String backend;
    @Autowired
    iUserService user ;
    @Autowired
    RestTemplate rest;

    @PostMapping("/createCart")
    public String create(@RequestParam("idProduct") Integer idProduct) {
        if (user.getUsername() == null)
            return "redirect:/user/createUser";

        CartRequest cartRequest = new CartRequest();
        cartRequest.setIdProduct(idProduct);
        cartRequest.setUsername(user.getUsername());

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/create")
                .buildAndExpand()
                .toUri();

        rest.postForEntity(uri, cartRequest, ResponseBase.class).getBody();
        return "redirect:/user/cartUser";
    }
}
