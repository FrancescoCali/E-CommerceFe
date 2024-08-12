package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.CartRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.GpuRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CartController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @GetMapping("/createCart")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-cart");
        CartRequest req = new CartRequest();
        req.setErrorMSG(null);
        mav.addObject("cart", req);

        return mav;
    }


}
