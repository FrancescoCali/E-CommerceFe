package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Controller
@RequestMapping("/components")
public class ComponentsController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @GetMapping(value = {"/listCpu"})
    public ModelAndView listCpu(@RequestParam(required = false) String role) {

        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-cpu");
        else
            mav = new ModelAndView("list-cpu-img");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cpu/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listCpu", resp);

        return mav;
    }

    @GetMapping (value = {"/listGpu"})
    public  ModelAndView listGpu(@RequestParam(required = false) String role) {

        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-gpu");
        else
            mav = new ModelAndView("list-gpu-img");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "gpu/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listGpu", resp);

        return mav;
    }


    @GetMapping (value = {"/listCooler"})
    public  ModelAndView listCooler(@RequestParam(required = false) String role) {
        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-cooler");
        else
            mav = new ModelAndView("list-cooler-img");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cooler/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listCooler", resp);

        return mav;
    }

    @GetMapping (value = {"/listMemory"})
    public  ModelAndView listMemory(@RequestParam(required = false) String role) {

        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-memory");
        else
            mav = new ModelAndView("list-memory-img");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "memory/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listMemory", resp);

        return mav;
    }

    @GetMapping (value = {"/listMotherboard"})
    public  ModelAndView listMotherboard(@RequestParam(required = false) String role) {

        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-motherboard");
        else
            mav = new ModelAndView("list-motherboard-img");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "motherboard/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listMotherboard", resp);

        return mav;
    }

    @GetMapping (value = {"/listPsu"})
    public  ModelAndView listPsu(@RequestParam(required = false) String role) {

        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-psu");
        else
            mav = new ModelAndView("list-psu-img");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "psu/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listPsu", resp);

        return mav;
    }

    @GetMapping (value = {"/listRam"})
    public  ModelAndView listRam(@RequestParam(required = false) String role) {

        ModelAndView mav;
        if (role.equalsIgnoreCase("ADMIN"))
            mav = new ModelAndView("list-ram");
        else
            mav = new ModelAndView("list-ram-img");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "ram/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listRam", resp);

        return mav;
    }
}
