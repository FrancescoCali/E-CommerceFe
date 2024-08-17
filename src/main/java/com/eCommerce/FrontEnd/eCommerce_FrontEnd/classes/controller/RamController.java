package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.RamView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.RamRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

@Controller
@RequestMapping("/ram")
public class RamController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(RamController.class);

    @GetMapping("/createRam")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-ram");
        RamRequest req = new RamRequest();
        req.setErrorMSG(null);
        mav.addObject("ram", req);

        return mav;
    }

    @GetMapping (value = {"/listRam"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "ram/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listRam", resp);

        return mav;
    }
    @PostMapping("/saveRam")
    public Object save(@ModelAttribute("ram") RamRequest req){
        URI uri ;
        if(req.getId()==null)
            uri = UriComponentsBuilder
                .fromHttpUrl(backend + "ram/create")
                .buildAndExpand()
                .toUri();
        else
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "ram/update")
                    .buildAndExpand()
                    .toUri();

        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-ram");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("ram", req);
            return mav;
        }
        return "redirect:/ram/listRam";
    }

    @GetMapping("/removeRam")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl("/ram/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/ram/listRam";
    }

    @GetMapping("/updateRam")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-ram");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "ram/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<RamView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        RamRequest req = (RamRequest) convertInObject(resp.getDati(),RamRequest.class);

        mav.addObject("ram",req);
//        mav.addObject("myTitle", "Modifica ram");
        return mav;
    }
}
