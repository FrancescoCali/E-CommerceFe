package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.CartRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

@Controller
public class CartController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    iUserService user ;

    @Autowired
    RestTemplate rest;

    @GetMapping("/createCart")
    public Object create(@RequestParam(required = false) Integer id){

        ModelAndView mav = new ModelAndView("create-cart");

        if (id == null)
            return "redirect:/user/createUser";

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        UserRequest req = (UserRequest) convertInObject(resp.getDati(),UserRequest.class);


        CartRequest cartRequest = new CartRequest();
        req.setErrorMSG(null);
        mav.addObject("cart", req);
        return mav;
    }


}
