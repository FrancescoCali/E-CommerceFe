package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.ProductView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
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

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

@Controller
@RequestMapping("/productUser")
public class ProductUserController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @Autowired
    iUserService user;

    public static Logger log = LoggerFactory.getLogger(ProductUserController.class);

    //ITEM CAMBIA IN BASE AL CLICK SUL CAROSELLO, SI ASPETTA UN PARAMETRO PASSATO DAL CAROSELLO
    @GetMapping ("/list")
    public  ModelAndView list(@RequestParam(name = "item" ,required = true) String item) {
        ModelAndView mav;
        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("listUser/list-"+item+"-img");
        else
            mav = new ModelAndView("listUser/list-"+item);

        if(!item.equalsIgnoreCase("components")){
            URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + item +"/list")
                .buildAndExpand().toUri();

            Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
            mav.addObject("listProduct", resp);
        }
        mav.addObject(item,item);
        mav.addObject("username",user.getUsername());
        mav.addObject("role", user.getRole());
        return mav;
    }

    @GetMapping("/page")
    public ModelAndView page(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("pages/product-page");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/getById")
                .queryParam("id", id)
                .buildAndExpand().toUri();
        ResponseObject<ProductView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        ProductView view = (ProductView) convertInObject(resp.getDati(), ProductView.class);
        log.debug(view.getUrl());
        mav.addObject("view", view);
        return mav;
    }
}
