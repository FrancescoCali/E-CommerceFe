package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
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
    iUserService user ;

    @Autowired
    RestTemplate rest;

    @GetMapping(value = {"/listComponents"})
    public ModelAndView listComponents(@RequestParam(required=false) String username,@RequestParam(required = false) String role) {
         return new ModelAndView("list-components-img");
    }

    @GetMapping ("/listCpu")
    public  ModelAndView list( ) {
        ModelAndView mav ;

        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-cpu-img");
        else
            mav = new ModelAndView("list-cpu");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "pc/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listCpu", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());
        return mav;
    }


    @GetMapping (value = {"/listGpu"})
    public  ModelAndView listGpu(@RequestParam(required=false) String username,@RequestParam(required = false) String role) {

        ModelAndView mav;
        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-gpu-img");
        else
            mav = new ModelAndView("list-gpu");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "gpu/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listGpu", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());
        return mav;
    }


    @GetMapping (value = {"/listCooler"})
    public  ModelAndView listCooler() {
        ModelAndView mav;
        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-cooler-img");
        else
            mav = new ModelAndView("list-cooler");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cooler/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listCooler", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());
        return mav;
    }

    @GetMapping (value = {"/listMemory"})
    public  ModelAndView listMemory() {

        ModelAndView mav;
        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-memory-img");
        else
        mav = new ModelAndView("list-memory");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "memory/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listMemory", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());


        return mav;
    }

    @GetMapping (value = {"/listMotherboard"})
    public  ModelAndView listMotherboard() {

        ModelAndView mav;

        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-motherboard-img");
        else
            mav = new ModelAndView("list-motherboard");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "motherboard/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listMotherboard", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());


        return mav;
    }

    @GetMapping (value = {"/listPsu"})
    public  ModelAndView listPsu() {

        ModelAndView mav;
        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-psu-img");
        else
            mav = new ModelAndView("list-psu");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "psu/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listPsu", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());


        return mav;
    }

    @GetMapping (value = {"/listRam"})
    public  ModelAndView listRam() {

        ModelAndView mav;
        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-ram-img");
        else
            mav = new ModelAndView("list-ram");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "ram/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listRam", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());


        return mav;
    }
}
