package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.MotherboardRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.MotherboardView;
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
import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

import java.net.URI;

@Controller
@RequestMapping("/motherboard")
public class MotherboardController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(MotherboardController.class);

    @GetMapping("/createMotherboard")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-motherboard");
        MotherboardRequest req = new MotherboardRequest();
        req.setErrorMSG(null);
        mav.addObject("motherboard", req);

        return mav;
    }

    @PostMapping("/saveMotherboard")
    public Object save(@ModelAttribute("motherboard") MotherboardRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "motherboard/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-motherboard");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("motherboard", req);
            return mav;
        }
        return "redirect:/components/listMotherboard";
    }

    @GetMapping("/removeMotherboard")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/motherboard/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/components/listMotherboard";
    }

    @GetMapping("/updateMotherboard")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-motherboard");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "motherboard/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<MotherboardView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        MotherboardRequest req = (MotherboardRequest) convertInObject(resp.getDati(),MotherboardRequest.class);

        mav.addObject("motherboard",req);
        return mav;
    }
}
