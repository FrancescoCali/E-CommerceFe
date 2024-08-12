package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.MouseRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view.MouseView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.response.ResponseObject;
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

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.utilities.WebUtils.convertInObject;

@Controller
@RequestMapping("/mouse")
public class MouseController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(MouseController.class);

    @GetMapping("/createMouse")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-mouse");
        MouseRequest req = new MouseRequest();
        req.setErrorMSG(null);
        mav.addObject("mouse", req);

        return mav;
    }

    @GetMapping (value = {"/listMouse"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "mouse/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listMouse", resp);

        return mav;
    }
    @PostMapping("/saveMouse")
    public Object save(@ModelAttribute("mouse") MouseRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "mouse/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-mouse");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("mouse", req);
            return mav;
        }
        return "redirect:/mouse/listMouse";
    }

    @GetMapping("/removeMouse")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/mouse/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/mouse/listMouse";
    }

    @GetMapping("/updateMouse")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-mouse");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "mouse/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<MouseView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        MouseRequest req = (MouseRequest) convertInObject(resp.getDati(),MouseRequest.class);

        mav.addObject("mouse",req);
        mav.addObject("myTitle", "Modifica mouse");
        return mav;
    }
}
