package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @GetMapping("/createUser")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("login");
        UserRequest req = new UserRequest();
        req.setErrorMSG(null);
        req.setRole("USER");
        mav.addObject("user", req);
        mav.addObject("username", req.getUsername());
        mav.addObject("role", req.getRole());
        return mav;
    }

    @PostMapping("/saveUser")
    public Object save(@ModelAttribute("user") UserRequest req){

        URI uri = (req.getUsername() == null) ?
                UriComponentsBuilder.fromHttpUrl(backend + "user/create").buildAndExpand().toUri() :
                UriComponentsBuilder.fromHttpUrl(backend + "user/update").buildAndExpand().toUri();

        ResponseBase resp = rest.postForEntity(uri, req, ResponseBase.class).getBody();
        System.out.println(req.getUsername());
        if (!resp.getRc()) {
            // In caso di errore, visualizza la pagina di registrazione con un messaggio di errore
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("user", req);
            mav.addObject("errorMessage", "Errore durante la registrazione. Per favore, riprova.");
            return mav;
        }

        if (req.getUsername() == null) {
            // Creazione di un nuovo utente
            user.createUser(req);
        } else {
            // Aggiornamento di un utente esistente
            user.updateUser(req);
            user.setUsername(req.getUsername());
        }
        // Redirect alla home page
        return "redirect:/home";
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
