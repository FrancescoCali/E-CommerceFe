package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.CpuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view.CpuView;
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
import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.utilities.WebUtils.convertInObject;

import java.net.URI;

@Controller
@RequestMapping("/cpu")
public class CpuController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(CpuController.class);


    @GetMapping (value = {"/listCpu"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cpu/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listCpu", resp);

        return mav;
    }

    @GetMapping("/createCpu")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-cpu");
        CpuRequest req = new CpuRequest();
        req.setErrorMSG(null);
        mav.addObject("cpu", req);
        return mav;
    }

    @PostMapping("/saveCpu")
    public Object save(@ModelAttribute("cpu") CpuRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cpu/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-cpu");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("cpu", req);
            return mav;
        }
        return "redirect:/cpu/listCpu";
    }

    @GetMapping("/removeCpu")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/cpu/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/cpu/listCpu";
    }

    @GetMapping("/updateCpu")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-cpu");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cpu/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<CpuView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        CpuRequest req = (CpuRequest) convertInObject(resp.getDati(),CpuRequest.class);

        mav.addObject("cpu",req);
        mav.addObject("myTitle", "Modifica cpu");
        return mav;
    }
}
