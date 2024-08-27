package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.PcRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.PcView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
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
@RequestMapping("/pc")
public class  PcController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    iUserService user ;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(PcController.class);

    @GetMapping("/createPc")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-pc");
        PcRequest req = new PcRequest();
        req.setErrorMSG(null);
        mav.addObject("pc", req); 
        return mav;
    }

    @GetMapping ("/listPc")
    public  ModelAndView list( ) {
        ModelAndView mav ;

        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-pc-img");
        else
            mav = new ModelAndView("list-pc");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "pc/list")
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listPc", resp);
        mav.addObject("role",  user.getRole() );
        mav.addObject("username", user.getUsername() );
        return mav;
    }

    @PostMapping("/savePc")
    public Object save(@ModelAttribute("pc") PcRequest req){

        URI uri ;
        if(req.getIdProduct()==null)
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "pc/create")
                    .buildAndExpand()
                    .toUri();
        else
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "pc/update")
                    .buildAndExpand()
                    .toUri();

        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-pc");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("pc", req);
            return mav;
        }
        return "redirect:/pc/listPc";
    }

    @GetMapping("/removePc")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/pc/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/pc/listPc";
    }

    @GetMapping("/updatePc")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-pc");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "pc/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<PcView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        PcRequest req = (PcRequest) convertInObject(resp.getDati(),PcRequest.class);

        mav.addObject("pc",req);
        mav.addObject("myTitle", "Modifica pc");
        return mav;
    }
    
}
