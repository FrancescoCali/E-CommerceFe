package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.KeyboardRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.KeyboardView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
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
@RequestMapping("/keyboard")

public class KeyboardController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(KeyboardController.class);

    @GetMapping("/createKeyboard")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-keyboard");
        KeyboardRequest req = new KeyboardRequest();
        req.setErrorMSG(null);
        mav.addObject("keyboard", req);

        return mav;
    }

    @GetMapping (value = {"/listKeyboard"})
    public  ModelAndView list(@RequestParam(required = false) String role) {
        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-keyboard");
        else
            mav = new ModelAndView("list-keyboard-img");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "keyboard/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listKeyboard", resp);

        return mav;
    }

    @PostMapping("/saveKeyboard")
    public Object save(@ModelAttribute("keyboard") KeyboardRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "keyboard/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-keyboard");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("keyboard", req);
            return mav;
        }
        return "redirect:/keyboard/listKeyboard";
    }

    @GetMapping("/removeKeyboard")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/keyboard/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/keyboard/listKeyboard";
    }

    @GetMapping("/updateKeyboard")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-Keyboard");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "keyboard/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<KeyboardView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        KeyboardRequest req = (KeyboardRequest) convertInObject(resp.getDati(),KeyboardRequest.class);

        mav.addObject("keyboard",req);
        mav.addObject("myTitle", "Modifica keyboard");
        return mav;
    }
}
