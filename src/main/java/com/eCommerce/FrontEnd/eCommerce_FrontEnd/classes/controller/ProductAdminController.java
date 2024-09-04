package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.ProductRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@Controller
@RequestMapping("/admin/product")
public class ProductAdminController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @Autowired
    MyUserDetailsService user;

    @GetMapping("/create")
    public ModelAndView create( @RequestParam (required = true ) String item ){
        ModelAndView mav = new ModelAndView("create-update/create-update-product");
        ProductRequest req = new ProductRequest();
        req.setErrorMSG(null);
        req.setItem(item);
        mav.addObject("product",req);
        mav.addObject("username",user.getUsername());
        mav.addObject("role",user.getRole());
        return mav;
    }

    @PostMapping("/save")
    public Object save(@ModelAttribute("product") ProductRequest req ){
        URI uri ;
        if(req.getIdProduct()==null)
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "product/create")
                    .buildAndExpand()
                    .toUri();
        else
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend +"product/update")
                    .buildAndExpand()
                    .toUri();
        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();
        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-update/create-update-product");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("req", req);
            return mav;
        }
        return "redirect:/admin/product/list?item="+req.getItem();
    }

    @GetMapping("/removeProduct")
    public Object removeProduct(@RequestParam Integer id,@RequestParam (required = true ) String item){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/remove")
                .queryParam("id",id)
                .queryParam("item",item)
                .buildAndExpand()
                .toUri();
        rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/admin/product/list";
    }

    @GetMapping("/removeItem")
    public Object removeItem(@RequestParam Integer idProduct ,@RequestParam (required = true ) String item){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/remove")
                .queryParam("id",idProduct)
                .queryParam("item",item)
                .buildAndExpand()
                .toUri();
        rest.postForEntity(uri,idProduct, ResponseBase.class).getBody();
        return "redirect:/admin/product/list";
    }

    @GetMapping("/updateProduct")
    public ModelAndView updateProduct(@RequestParam Integer idProduct , @RequestParam (required = true ) String item) {
        ModelAndView mav = new ModelAndView("create-update/create-update-product");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/getById")
                .queryParam("idProduct", idProduct)
                .buildAndExpand()
                .toUri();
        mav.addObject("product", rest.getForEntity(uri, ResponseObject.class).getBody());
        return mav;
    }

    @GetMapping("/updateItem")
    public ModelAndView updateItem(@RequestParam Integer id , @RequestParam (required = true ) String item) {
        ModelAndView mav = new ModelAndView("create-update/create-update-product");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();
        mav.addObject("product", rest.getForEntity(uri, ResponseObject.class).getBody());
        return mav;
    }

    @GetMapping("/list")
    public ModelAndView listProductModels(@RequestParam (required = true ) String item){
        ModelAndView mav=new ModelAndView("listAdmin/list-product");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/list")
                .queryParam("item",item )
                .buildAndExpand().toUri();
        Response<?> resp = rest.getForEntity(uri,Response.class).getBody();
        mav.addObject("list", resp );
        mav.addObject("item",item);
        return mav;
    }
}
