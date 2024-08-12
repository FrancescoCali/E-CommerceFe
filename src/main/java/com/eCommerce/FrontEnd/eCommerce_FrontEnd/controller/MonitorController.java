package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.GpuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.MonitorRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.MotherboardRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view.MonitorView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.view.MotherboardView;
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
@RequestMapping("monitor")
public class MonitorController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(MonitorController.class);

    @GetMapping("/createMonitor")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-monitor");
        MonitorRequest req = new MonitorRequest();
        req.setErrorMSG(null);
        mav.addObject("monitor", req);

        return mav;
    }

    @GetMapping (value = {"/listMonitor"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "monitor/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listMonitor", resp);

        return mav;
    }

    @PostMapping("/saveMonitor")
    public Object save(@ModelAttribute("monitor") MonitorRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl("monitor/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-monitor");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("monitor", req);
            return mav;
        }
        return "redirect:/monitor/listMonitor";
    }

    @GetMapping("/removeMonitor")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl("/monitor/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/monitor/listMonitor";
    }

    @GetMapping("/updateMonitor")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-monitor");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "monitor/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<MonitorView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        MonitorRequest req = (MonitorRequest) convertInObject(resp.getDati(),MonitorRequest.class);

        mav.addObject("monitor",req);
        return mav;
    }
}
