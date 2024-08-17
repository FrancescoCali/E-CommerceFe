package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginPageController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @GetMapping("/login")
    public ModelAndView login(){
        return new ModelAndView("login") ;
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "Error";
    }
}