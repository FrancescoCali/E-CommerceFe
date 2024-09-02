package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
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
    iUserService user;

    @Autowired
    RestTemplate rest;

    @PostMapping("/saveUser")
    public Object save(@ModelAttribute("user") UserRequest req, @RequestParam(required = false) String param) {
        System.out.println(req.getUsername());
        URI checkUri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/checkByUsername")
                .queryParam("username", req.getUsername())
                .buildAndExpand()
                .toUri();

        ResponseObject<Boolean> checkResponse = rest.getForEntity(checkUri, ResponseObject.class).getBody();
        boolean bool = checkResponse.getDati();
        System.out.println();
        System.out.println();

        System.out.println("stampa di bool linea 45"+bool);
        System.out.println();
        System.out.println();

        URI uri;
        if (param == null && bool) {
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "user/update")
                    .buildAndExpand()
                    .toUri();

        } else if (param.equalsIgnoreCase("create") && !bool) {
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
            ModelAndView mav = (!bool) ?
                    new ModelAndView("login") : new ModelAndView("userManager/update-user");
            req.setErrorMSG(resp != null ? resp.getMsg() : "An error occurred");
            mav.addObject("user", req);
            mav.addObject("errorMSG", req.getErrorMSG()); // Aggiunta dell'errore
            return mav;
        }
        ModelAndView home = new ModelAndView("home/home-user");
        home.addObject("username", user.getUsername());
        if (!bool) {
            user.createUser(req);
            return home;
        } else {
            user.updateUser(req);
            user.setUsername(req.getUsername());
            return home;
        }
    }

    @GetMapping("/removeUser")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();
        @SuppressWarnings("unchecked")
        ResponseObject<UserRequest> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        UserRequest req = (UserRequest) convertInObject(resp.getDati(),UserRequest.class);
        user.removeUser(req);
        uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/user/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();
        rest.postForEntity(uri,id,ResponseBase.class) ;
        return "redirect:/home";
    }

    @GetMapping("/updateUser")
    public ModelAndView update() {
        ModelAndView mav = new ModelAndView("userManager/create-update-user");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getByUsername")
                .queryParam("username", user.getUsername())
                .buildAndExpand()
                .toUri();
        ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        mav.addObject("user", resp );
        return mav;
    }

    @GetMapping("/accountUser")
    public ModelAndView profile(){
        ModelAndView mav = new ModelAndView("userManager/profile");
        URI uri=UriComponentsBuilder
                .fromHttpUrl(backend + "user/getByUsername")
                .queryParam("username", user.getUsername())
                .buildAndExpand()
                .toUri();
        @SuppressWarnings("unchecked")
        ResponseObject<UserRequest> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        UserRequest req = (UserRequest) convertInObject(resp.getDati(),UserRequest.class);
        mav.addObject("user", req);
        mav.addObject("username",user.getUsername());
        mav.addObject("role",user.getRole());
        return mav;
    }
    //CARRELLO DELL'UTENTE
    @GetMapping("/cartUser")
    public ModelAndView cart(){
        ModelAndView mav = new ModelAndView("userManager/cart-user");
        mav.addObject("username",user.getUsername());
        mav.addObject("role",user.getRole());
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cart/list")
                .queryParam("username", user.getUsername() )
                .buildAndExpand()
                .toUri();
        Response<?> resp = rest.getForEntity(uri,Response.class).getBody()  ;
        mav.addObject("cartList",resp);
        return mav;
    }

}
