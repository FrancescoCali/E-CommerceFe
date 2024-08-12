package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.GpuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.LaptopRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.PcRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view.LaptopView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view.PcView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.response.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.utilities.WebUtils.convertInObject;

@Controller
public class LaptopController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(CpuController.class);

    @GetMapping("/createLaptop")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-laptop");
        LaptopRequest req = new LaptopRequest();
        req.setErrorMSG(null);
        mav.addObject("laptop", req);

        return mav;
    } 
    
    @GetMapping (value = {"/listLaptop"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "laptop/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listLaptop", resp);

        return mav;
    }

    @PostMapping("/saveLaptop")
    public Object save(@ModelAttribute("laptop") LaptopRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "laptop/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-laptop");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("laptop", req);
            return mav;
        }
        return "redirect:/laptop/listLaptop";
    }

    @GetMapping("/removeLaptop")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/laptop/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/laptop/listLaptop";
    }

    @GetMapping("/updateLaptop")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-laptop");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "laptop/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<LaptopView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        LaptopRequest req = (LaptopRequest) convertInObject(resp.getDati(),LaptopRequest.class);

        mav.addObject("laptop",req);
//        mav.addObject("myTitle", "Modifica laptop");
        return mav;
    }

}
