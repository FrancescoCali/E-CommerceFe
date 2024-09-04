package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.CartRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.CartView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.security.MyUserDetailsService;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
    MyUserDetailsService user ;
    @Autowired
    RestTemplate rest;

    @PostMapping("/createCart")
    public String create(@RequestParam("idProduct") Integer idProduct) {
        if (user.getUsername() == null)
            return "redirect:/user/createUser";
        CartRequest cartRequest = new CartRequest();
        cartRequest.setIdProduct(idProduct);
        cartRequest.setUsername(user.getUsername());
        cartRequest.setSelected(true);
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/create")
                .buildAndExpand()
                .toUri();
        rest.postForEntity(uri, cartRequest, ResponseBase.class).getBody();
        return "redirect:/user/cartUser";
    }

    @PostMapping("/removeCart")
    public ModelAndView remove(@RequestParam (required = false) Integer id){
        ModelAndView mav = new ModelAndView("userManager/cart-user");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/getById")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();
        @SuppressWarnings("unchecked")
        ResponseObject<CartView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        CartView view = (CartView) convertInObject(resp.getDati(),CartView.class);
        CartRequest req=new CartRequest();
        req.setId(view.getId());
        req.setIdProduct(view.getIdProduct());
        uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/cart/remove")
                .buildAndExpand()
                .toUri();
        rest.postForEntity(uri,req,ResponseBase.class) ;
        uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/cart/list")
                .queryParam("username",view.getUsername())
                .buildAndExpand()
                .toUri();
        Response<?>resp2=rest.getForEntity(uri,Response.class).getBody();
        mav.addObject("cartList",resp2) ;
        mav.addObject("username",user.getUsername());
        mav.addObject("role",user.getRole());
        System.out.println(resp2.getDati());
        return mav ;
    }

    @GetMapping("/select")
    public ResponseEntity<ResponseBase> select(@RequestParam Integer id) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/cart/select")
                .queryParam("id", id)
                .build()
                .toUri();
        ResponseEntity<ResponseBase> response = rest.getForEntity(uri, ResponseBase.class);
        return response;
    }

    /*********/
    @GetMapping("/confirmPurchase")
    public ModelAndView confirmPurchase(){
        ModelAndView mav = new ModelAndView("userManager/order-summary");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/listSelectedProducts")
                .queryParam("username",user.getUsername())
                .buildAndExpand()
                .toUri();
        mav.addObject("cartList",rest.getForEntity(uri,Response.class).getBody());
        mav.addObject("username",user.getUsername());
        mav.addObject("role",user.getRole());
        return mav ;
    }

    @GetMapping("/purchaseConfirmed")
    public ModelAndView purchaseConfirmed() {

        ModelAndView mav = new ModelAndView("userManager/purchase-confirmed");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/purchaseConfirmed")
                .queryParam("username", user.getUsername())
                .buildAndExpand()
                .toUri();

        rest.getForEntity(uri, ResponseBase.class);
        mav.addObject("username", user.getUsername());
        mav.addObject("role", user.getRole());
        return mav;
    }
}
