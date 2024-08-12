package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.GpuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.PcRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PcController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @GetMapping("/createPc")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-pc");
        PcRequest req = new PcRequest();
        req.setErrorMSG(null);
        mav.addObject("pc", req);

        return mav;
    }
}
