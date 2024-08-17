package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.CoolerRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.CoolerView;
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
@RequestMapping("/cooler")
public class CoolerController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(CoolerController.class);

    @GetMapping("/createCooler")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-cooler");
        CoolerRequest req = new CoolerRequest();
        req.setErrorMSG(null);
        mav.addObject("cooler", req);

        return mav;
    }

    @GetMapping (value = {"/listCooler"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cooler/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listCooler", resp);

        return mav;
    }

    @PostMapping("/saveCooler")
    public Object save(@ModelAttribute("cooler") CoolerRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cooler/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-cooler");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("cooler", req);
            return mav;
        }
        return "redirect:/cooler/listCooler";
    }

    @GetMapping("/removeCooler")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/cooler/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/cooler/listCooler";
    }

    @GetMapping("/updateCooler")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-cooler");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cooler/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<CoolerView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        CoolerRequest req = (CoolerRequest) convertInObject(resp.getDati(),CoolerRequest.class);

        mav.addObject("cooler",req);
        mav.addObject("myTitle", "Modifica cooler");
        return mav;
    }
}
