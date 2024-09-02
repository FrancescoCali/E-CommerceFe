package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.ProductRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
<<<<<<< HEAD
import java.net.URI;
import java.util.List;
import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;
=======

import java.net.URI;
>>>>>>> 6b62a7f948d8e319b2c2a5db8ccf276afd0a6dfc

@Controller
public class HomeController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    iUserService user ;

    @Autowired
    RestTemplate rest;




    @GetMapping("/home")
    public ModelAndView homepage() {
        ModelAndView mav;
        if(user.getRole() != null) {
            if (user.getRole().equalsIgnoreCase("ROLE_ADMIN"))
                mav = new ModelAndView("home/home-admin");
            else
                mav = new ModelAndView("home/home-user");
        }
        else
            mav = new ModelAndView("home/home-user");

        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());
        return mav;
    }

    @GetMapping("/logout")
    public   ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
            new SecurityContextLogoutHandler().logout(request, response, auth);
        ModelAndView mav=new ModelAndView("home/return-home");
        return mav;
    }


}
