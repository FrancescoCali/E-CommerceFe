package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.ProductRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.ProductView;
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

import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/createProduct")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-product");
        ProductRequest req = new ProductRequest();
        req.setErrorMSG(null);
        mav.addObject("product", req);

        return mav;
    }

    @GetMapping (value = {"/listProduct"})
    public  ModelAndView list() {

        ModelAndView mav = new ModelAndView("home");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/list")
                .buildAndExpand().toUri();
        log.debug("URI:" + uri);

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listProduct", resp);

        return mav;
    }

    @PostMapping("/saveProduct")
    public Object save(@ModelAttribute("product") ProductRequest req){

        URI uri ;
        if(req.getIdProduct()==null)
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "product/create")
                    .buildAndExpand()
                    .toUri();
        else
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "product/update")
                    .buildAndExpand()
                    .toUri();

        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-product");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("product", req);
            return mav;
        }
        return "redirect:/product/listProduct";
    }

    @GetMapping("/removeProduct")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/product/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/product/listProduct";
    }

    @GetMapping("/updateProduct")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-product");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<ProductView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        ProductRequest req = (ProductRequest) convertInObject(resp.getDati(),ProductRequest.class);

        mav.addObject("product",req);
//        mav.addObject("myTitle", "Modifica product");
        return mav;
    }
}
