package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.security.MyUserDetailsService;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
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
@RequestMapping("/user")
public class UserController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    MyUserDetailsService user;

    @Autowired
    RestTemplate rest;

    @PostMapping("/saveCreate")
    public Object saveCreate(@ModelAttribute("user") UserRequest req) {

        URI checkUri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/checkByUsername")
                .queryParam("username", req.getUsername())
                .buildAndExpand()
                .toUri();

        ResponseObject<Boolean> checkResponse = rest.getForEntity(checkUri, ResponseObject.class).getBody();
        boolean bool = checkResponse.getDati();

        URI uri;
        if (!bool) {
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "user/create")
                    .buildAndExpand()
                    .toUri();
        } else {
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("errorMSG", "Cannot create or update user: Username already exists or invalid operation.");
            return mav;
        }
        ResponseBase resp = rest.postForObject(uri, req, ResponseBase.class);
        if (resp == null || !resp.getRc()) {
            ModelAndView mav = new ModelAndView("login");
            req.setErrorMSG(resp != null ? resp.getMsg() : "An error occurred");
            mav.addObject("user", req);
            mav.addObject("errorMSG", req.getErrorMSG()); // Aggiunta dell'errore
            return mav;
        }
        ModelAndView home = new ModelAndView("home/home-user");
        req.setRole("USER");
        home.addObject("username", req.getUsername());
        home.addObject("role", req.getRole());
        return home;
    }

    @PostMapping("/saveUpdate")
    public Object saveUpdate(@ModelAttribute("user") UserRequest req) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/update")
                .buildAndExpand()
                .toUri();
        ResponseBase resp = rest.postForObject(uri, req, ResponseBase.class);
        if (resp == null || !resp.getRc()) {
            ModelAndView mav = new ModelAndView("userManager/update-user");
            req.setErrorMSG(resp != null ? resp.getMsg() : "An error occurred");
            mav.addObject("user", req);
            mav.addObject("errorMSG", req.getErrorMSG());
            return mav;
        }
        ModelAndView home = new ModelAndView("home/home-user");
        home.addObject("username", user.getUsername());
        home.addObject("role", user.getRole());
        return home;
    }

    @GetMapping("/removeUser")
    public Object remove(@RequestParam Integer id) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();
        @SuppressWarnings("unchecked")
        ResponseObject<UserRequest> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        UserRequest req = (UserRequest) convertInObject(resp.getDati(), UserRequest.class);
        uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/user/remove")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();
        rest.postForEntity(uri, id, ResponseBase.class);
        return "redirect:/home";
    }

    @GetMapping("/accountUser")
    public ModelAndView profile() {
        ModelAndView mav = new ModelAndView("userManager/profile");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getByUsername")
                .queryParam("username", user.getUsername())
                .buildAndExpand()
                .toUri();
        @SuppressWarnings("unchecked")
        ResponseObject<UserRequest> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        UserRequest req = (UserRequest) convertInObject(resp.getDati(), UserRequest.class);
        mav.addObject("user", req);
        mav.addObject("username",user.getUsername());
        mav.addObject("role",user.getRole());
        return mav;
    }

    //CARRELLO DELL'UTENTE
    @GetMapping("/cartUser")
    public ModelAndView cart() {
        ModelAndView mav = new ModelAndView("userManager/cart-user");
        mav.addObject("username", user.getUsername());
        mav.addObject("role", user.getRole());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/list")
                .queryParam("username", user.getUsername())
                .buildAndExpand()
                .toUri();
        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("cartList", resp);
        return mav;
    }

}
