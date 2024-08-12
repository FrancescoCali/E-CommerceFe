package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.GpuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.LaptopRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

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


}
