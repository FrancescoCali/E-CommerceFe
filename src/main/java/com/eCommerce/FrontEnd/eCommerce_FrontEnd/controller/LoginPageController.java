package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginPageController {

    @GetMapping("/login")
    public ModelAndView login(){
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "Error";
    }

    @GetMapping("/home")
    public ModelAndView homepage(){
        ModelAndView mav = new ModelAndView("home");
        return mav;
    }
}