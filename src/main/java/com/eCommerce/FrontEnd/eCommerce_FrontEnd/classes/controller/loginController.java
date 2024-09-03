package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;

import java.net.URI;

@Controller
public class loginController {

    @Value("${eCommerce.backend}")
    String backend;
    @Autowired
    RestTemplate rest;
    @Autowired
    iUserService service;
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("user", new UserRequest());
        return "login";
    }

    @PostMapping("/access")
    public Object access(UserRequest req)
    {
        URI checkUri = UriComponentsBuilder
                //checkByUsername TI FA UN CONTROLLO SUL NOME SE ESISTE GIA
                .fromHttpUrl(backend + "user/checkByUsername")
                .queryParam("username", req.getUsername())
                .buildAndExpand()
                .toUri();
        ResponseObject<Boolean> checkResponse = rest.getForEntity(checkUri, ResponseObject.class).getBody();
        if(checkResponse.getDati())
        {
            req.setRole("USER");
            service.createUser(req);
            return "redirect:home";
        }
        ModelAndView mav=new ModelAndView("login");
        mav.addObject("user",req);
        mav.addObject("errorMSG","le credenziali inserite non sono valide");
        return mav;
    }
}