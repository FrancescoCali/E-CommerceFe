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
    public static Logger log = LoggerFactory.getLogger(RamController.class);

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

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();
        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("userManager/create-update-user");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("user", req);
            return mav;
        }
        if(req.getUsername()==null)
            user.createUser(req);
        else   {
            user.updateUser(req);
            user.setUsername( req.getUsername());
        }
        return "redirect:/home?";
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
        ResponseObject<UserView> resp;
            resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        if (resp == null || resp.getDati() == null) {
            log.error("La risposta del servizio è nulla o i dati sono nulli.");
            mav.addObject("errorMSG", "Nessun dato trovato per l'utente.");
            return mav;
        }
        UserRequest req  = (UserRequest) convertInObject(resp.getDati(), UserRequest.class);
        if (req  == null) {
            log.error("Errore nella conversione dell'oggetto.");
            mav.addObject("errorMSG", "Errore nella conversione dei dati utente.");
            return mav;
        }
        mav.addObject("user", req );
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
