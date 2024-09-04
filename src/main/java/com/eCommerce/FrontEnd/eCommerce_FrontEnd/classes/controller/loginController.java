package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
public class loginController {

    @Value("${eCommerce.backend}")
    String backend;
    @Autowired
    RestTemplate rest;
    @Autowired
    MyUserDetailsService service;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new UserRequest());
        return "login";
    }

}