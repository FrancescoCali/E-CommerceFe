package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.PsuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.PsuView;
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
import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

import java.net.URI;

@Controller
@RequestMapping("/psu")
public class PsuController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @Autowired
    iUserService user ;

    public static Logger log = LoggerFactory.getLogger(PsuController.class);

    @GetMapping("/createPsu")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-psu");
        PsuRequest req = new PsuRequest();
        req.setErrorMSG(null);
        mav.addObject("psu", req);

        return mav;
    }

    @PostMapping("/savePsu")
    public Object save(@ModelAttribute("psu") PsuRequest req){

        URI uri ;
        if(req.getId()==null)
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "psu/create")
                    .buildAndExpand()
                    .toUri();
        else
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "psu/update")
                    .buildAndExpand()
                    .toUri();

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-psu");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("psu", req);
            return mav;
        }
        return "redirect:/psu/listPsu";
    }

    @GetMapping("/removePsu")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/psu/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/psu/listPsu";
    }

    @GetMapping("/updatePsu")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-psu");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "psu/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<PsuView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        PsuRequest req = (PsuRequest) convertInObject(resp.getDati(),PsuRequest.class);

        mav.addObject("psu",req);
        return mav;
    }
}