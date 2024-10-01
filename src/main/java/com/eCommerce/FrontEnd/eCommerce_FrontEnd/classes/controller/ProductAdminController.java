package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.ProductRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.ProductView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.security.MyUserDetailsService;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Objects;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

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
                    .fromHttpUrl(backend + "product/update")
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
    public Object removeProduct(@RequestParam Integer id ,@RequestParam (required = true ) String item){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();
        rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/admin/product/list?item="+item;
    }

    @GetMapping("/updateProduct")
    public ModelAndView updateProduct(@RequestParam(required = true) Integer id, @RequestParam(required = true) String item) {
        ModelAndView mav = new ModelAndView("create-update/create-update-product");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        mav.addObject("username", user.getUsername());
        mav.addObject("role", user.getRole());
        mav.addObject("item", item);

        mav.addObject("product", WebUtils.convertInObject( rest.getForEntity(uri, ResponseObject.class).getBody().getDati(), ProductRequest.class));
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
    @GetMapping("/addProduct")
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestParam Integer id, @RequestParam Integer value) {
            URI uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "product/addProduct") // Controlla se questo è l'endpoint corretto
                    .queryParam("id", id)
                    .queryParam("value", value)
                    .buildAndExpand().toUri();
            ResponseEntity<ResponseBase> responseEntity = rest.getForEntity(uri, ResponseBase.class);
            ResponseBase resp = responseEntity.getBody();
            if (resp == null || !resp.getRc())
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to add quantity");
            return ResponseEntity.ok("Quantity added successfully");
    }
}
