package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.ProductRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.CoolerView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.ProductView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.PsuView;
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
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

@Controller
@RequestMapping("/admin/product")
public class ProductController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @Autowired
    iUserService user;

    public static Logger log = LoggerFactory.getLogger(ProductController.class);

    @GetMapping("/create")
    public ModelAndView create( @RequestParam (required = true ) String item ){
        ModelAndView mav = new ModelAndView("create-"+item);
        ProductRequest req = new ProductRequest();
        req.setErrorMSG(null);
        return mav;
    }


    @GetMapping("/page")
    public  ModelAndView page(@RequestParam Integer id ,@RequestParam (required = true ) String item) {

        ModelAndView mav=new ModelAndView(item+"-page");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + item +"/getById")
                .queryParam("id",id )
                .buildAndExpand().toUri();

        ResponseObject<ProductView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        ProductView view =(ProductView) convertInObject(resp , ProductView.class);
        mav.addObject( item, view );
        return mav;
    }

    @PostMapping("/save")
    public Object save(@ModelAttribute("product") ProductRequest req,@RequestParam (required = true ) String item){

        URI uri ;
        if(req.getIdProduct()==null)
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + item +"/create")
                    .buildAndExpand()
                    .toUri();
        else
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + item +"/update")
                    .buildAndExpand()
                    .toUri();

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-product");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("product", req);
            return mav;
        }
        return "redirect:/product/list";
    }

    @GetMapping("/remove")
    public Object remove(@RequestParam Integer id,@RequestParam (required = true ) String item){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + item + "/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/admin/product/list";
    }

    @GetMapping("/update")
    public ModelAndView update(@RequestParam Integer id,@RequestParam (required = true ) String item) {
        ModelAndView mav = new ModelAndView("create-update-product");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + item + "/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<ProductView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        ProductRequest req = (ProductRequest) convertInObject(resp.getDati(),ProductRequest.class);

        mav.addObject("product",req);
        return mav;
    }

    @GetMapping("/list")
    public ModelAndView listProductModels(@RequestParam (required = true ) String item){
        ModelAndView mav=new ModelAndView("listAdmin/list-product");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + item + "/list")
                .queryParam("item",item )
                .buildAndExpand().toUri();
        Response<?> resp = rest.getForEntity(uri,Response.class).getBody();
        mav.addObject("list", resp );
        return mav;
    }
}
