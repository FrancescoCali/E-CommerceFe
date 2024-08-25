package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    iUserService userService;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(RamController.class);

    @GetMapping("/createUser")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-update-user");
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

        URI uri = (req.getId() == null) ?
                UriComponentsBuilder.fromHttpUrl(backend + "user/create").buildAndExpand().toUri() :
                UriComponentsBuilder.fromHttpUrl(backend + "user/update").buildAndExpand().toUri();


        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();
        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-update-user");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("user", req);
            return mav;
        }
        if(req.getId()==null)
            userService.createUser(req);
        else
            userService.updateUser(req);

        return "redirect:/home";
    }

    @GetMapping("/removeUser")
    public Object remove(@RequestParam Integer id){

        /***************** recupero lo user *****************/
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<UserRequest> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        UserRequest req = (UserRequest) convertInObject(resp.getDati(),UserRequest.class);

        userService.removeUser(req);    /*** lo rimuovo dalla memoria ***/

        /**************** lo rimuovo dal db ****************/
        uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/user/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase respB = rest.postForEntity(uri,id,ResponseBase.class).getBody();

        return "redirect:/home";
    }

//    @GetMapping("/updateUser")
//    public ModelAndView update(@RequestParam String username) {
//        ModelAndView mav = new ModelAndView("create-update-user");
//
//        URI uri = UriComponentsBuilder
//                .fromHttpUrl(backend + "user/getByUsername")
//                .queryParam("username", username)
//                .buildAndExpand()
//                .toUri();
//
//        @SuppressWarnings("unchecked")
//        ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
//
//
//        UserRequest req2 = (UserRequest) convertInObject(resp.getDati(),UserRequest.class);
//        if(req2==null){
//            log.debug("QUEL PORCO DI DIO IN CROCE");
//        }else{
//            log.debug("dio cane bastardo figlio di puttana");
//        }
//        mav.addObject("user",req2);
//
//        log.debug(req2.getUsername());
//        log.debug(req2.getAddress());
//        log.debug(req2.getPassword());
//        log.debug(req2.getRole());
//
//        System.out.println("\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "STAMPA"+
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" +
//                "\n" );
//        return mav;
//    }
@GetMapping("/updateUser")
public ModelAndView update(@RequestParam String username) {
    ModelAndView mav = new ModelAndView("create-update-user");
    URI uri = UriComponentsBuilder
            .fromHttpUrl(backend + "user/getByUsername")
            .queryParam("username", username)
            .buildAndExpand()
            .toUri();

    ResponseObject<UserView> resp;
    try {
        resp = rest.getForEntity(uri, ResponseObject.class).getBody();
    } catch (Exception e) {
        log.error("Errore durante la chiamata al servizio: " + e.getMessage(), e);
        mav.addObject("errorMSG", "Errore durante il recupero dei dati utente.");
        return mav;
    }

    if (resp == null || resp.getDati() == null) {
        log.error("La risposta del servizio è nulla o i dati sono nulli.");
        mav.addObject("errorMSG", "Nessun dato trovato per l'utente.");
        return mav;
    }

    UserRequest req2 = (UserRequest) convertInObject(resp.getDati(), UserRequest.class);
    if (req2 == null) {
        log.error("Errore nella conversione dell'oggetto.");
        mav.addObject("errorMSG", "Errore nella conversione dei dati utente.");
        return mav;
    }

    mav.addObject("user", req2);
    log.debug("Username: " + req2.getUsername());
    log.debug("Address: " + req2.getAddress());
    log.debug("Password: " + req2.getPassword());
    log.debug("Role: " + req2.getRole());

    return mav;
}
    @GetMapping("/accountUser")
    public ModelAndView profile( @RequestParam String username,@RequestParam  String role){
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("username",username);
        mav.addObject("role",role);
        return mav;
    }
}
