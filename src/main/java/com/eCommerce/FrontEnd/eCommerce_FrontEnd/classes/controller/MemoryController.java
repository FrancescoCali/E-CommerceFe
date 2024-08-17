package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.MemoryRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.MemoryView;
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
@RequestMapping("/memory")

public class MemoryController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(MemoryController.class);

    @GetMapping("/createMemory")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-memory");
        MemoryRequest req = new MemoryRequest();
        req.setErrorMSG(null);
        mav.addObject("memory", req);

        return mav;
    }

    @GetMapping (value = {"/listMemory"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "memory/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listMemory", resp);

        return mav;
    }

    @PostMapping("/saveMemory")
    public Object save(@ModelAttribute("memory") MemoryRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "memory/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-memory");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("memory", req);
            return mav;
        }
        return "redirect:/memory/listMemory";
    }

    @GetMapping("/removeMemory")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/memory/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/memory/listMemory";
    }

    @GetMapping("/updateMemory")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-Memory");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "memory/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<MemoryView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        MemoryRequest req = (MemoryRequest) convertInObject(resp.getDati(),MemoryRequest.class);

        mav.addObject("memory",req);
        mav.addObject("myTitle", "Modifica memory");
        return mav;
    }
}
