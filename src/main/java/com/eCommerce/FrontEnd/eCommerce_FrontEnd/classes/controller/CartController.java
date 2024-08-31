package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.CartRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.CartView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

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

    @PostMapping("/removeCart")
    public Object remove(@RequestParam (required = false) Integer id, Boolean confirm){
        ModelAndView mav = new ModelAndView("userManager/cart-user");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/getById")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();
        @SuppressWarnings("unchecked")
        ResponseObject<CartView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        CartView req = (CartView) convertInObject(resp.getDati(),CartView.class);

        req.setConfirm(confirm); // elimino per conferma acquisto o perché non lo voglio comprare

        uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/cart/remove")
                .buildAndExpand()
                .toUri();
        mav.addObject("req",req) ;
        rest.postForEntity(uri,req,Response.class) ;

        return mav ;
    }
}
