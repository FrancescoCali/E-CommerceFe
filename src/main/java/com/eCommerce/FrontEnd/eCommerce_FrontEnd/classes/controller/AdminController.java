package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.PsuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
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
@RequestMapping("/admin")
public class AdminController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    iUserService userService;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/createUser")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-update-user");
        UserRequest req = new UserRequest();
        req.setErrorMSG(null);
        req.setRole("ADMIN");
        mav.addObject("user", req);
        return mav;
    }

    @GetMapping ("/listUser")
    public  ModelAndView list(@RequestParam String req) {
        ModelAndView mav = new ModelAndView("list-user");
        URI uri = null ;
        if(req.equalsIgnoreCase("ADMIN"))
            uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/listAdmin")
                .buildAndExpand().toUri();
        else
            uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/listUser")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listUser", resp);
        return mav;
    }

    @PostMapping("/saveUser")
    public Object save(@ModelAttribute("user") UserRequest req){
        URI uri ;
        if(req.getId()==null)
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "user/create")
                    .buildAndExpand()
                    .toUri();
        else
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "user/update")
                    .buildAndExpand()
                    .toUri();

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-user");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("user", req);
            return mav;
        }
        userService.createUser(req);
        return "redirect:/admin/listUser";
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
        ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        UserRequest req = (UserRequest) convertInObject(resp.getDati(),UserRequest.class);

        userService.removeUser(req);    /*** lo rimuovo dalla memoria ***/

        /**************** lo rimuovo dal db ****************/
        uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/user/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase respB = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/admin/listUser";
    }

    @GetMapping("/updateUser")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-user");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();

        UserRequest req = (UserRequest) convertInObject(resp.getDati(),UserRequest.class);
        mav.addObject("user",req);

        userService.updateUser(req);

        return mav;
    }
}