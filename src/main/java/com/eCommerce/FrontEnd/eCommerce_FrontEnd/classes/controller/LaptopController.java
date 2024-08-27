package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.LaptopRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.LaptopView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseBase;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

@Controller
@RequestMapping("/laptop")
public class LaptopController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    iUserService user ;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(CpuController.class);

    @GetMapping("/createLaptop")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("create-laptop");
        LaptopRequest req = new LaptopRequest();
        req.setErrorMSG(null);
        mav.addObject("laptop", req);

        return mav;
    }

    @GetMapping("/listLaptop")
    public ModelAndView list( ) {

        ModelAndView mav;

        if(user.getRole() == null || user.getRole().equalsIgnoreCase("ROLE_USER")  )
            mav = new ModelAndView("list-laptop-img");
        else
            mav = new ModelAndView("list-laptop");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "laptop/list") // product/list
                .buildAndExpand().toUri();

        Response<?> resp = rest.getForEntity(uri, Response.class).getBody();
        mav.addObject("listLaptop", resp);
        mav.addObject("role", user.getRole());
        mav.addObject("username",user.getUsername());
        return mav;
    }

    @PostMapping("/saveLaptop")
    public Object save(@ModelAttribute("laptop") LaptopRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "laptop/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-laptop");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("laptop", req);
            return mav;
        }
        return "redirect:/laptop/listLaptop";
    }

    @GetMapping("/removeLaptop")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/laptop/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id, ResponseBase.class).getBody();
        return "redirect:/laptop/listLaptop";
    }

    @GetMapping("/updateLaptop")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-laptop");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "laptop/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<LaptopView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        LaptopRequest req = (LaptopRequest) convertInObject(resp.getDati(),LaptopRequest.class);

        mav.addObject("laptop",req);
//        mav.addObject("myTitle", "Modifica laptop");
        return mav;
    }

}
