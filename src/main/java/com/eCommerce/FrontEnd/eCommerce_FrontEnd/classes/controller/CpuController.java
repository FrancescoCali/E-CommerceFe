package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.CpuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.CoolerView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.CpuView;
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
import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

import java.net.URI;

@Controller
@RequestMapping("/cpu")
public class CpuController {
    @Value("${eCommerce.backend}")
    String backend;
    @Autowired
    iUserService user ;
    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(CpuController.class);

    @GetMapping("/createCpu")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-cpu");
        CpuRequest req = new CpuRequest();
        req.setErrorMSG(null);
        mav.addObject("cpu", req);
        return mav;
    }

    @PostMapping("/saveCpu")
    public Object save(@ModelAttribute("cpu") CpuRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cpu/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-cpu");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("cpu", req);
            return mav;
        }
        return "redirect:/components/listCpu";
    }

    @GetMapping("/removeCpu")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/cpu/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/components/listCpu";
    }

    @GetMapping("/updateCpu")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-cpu");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "cpu/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<CpuView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        CpuRequest req = (CpuRequest) convertInObject(resp.getDati(),CpuRequest.class);

        mav.addObject("cpu",req);
        mav.addObject("myTitle", "Modifica cpu");
        return mav;
    }

    @GetMapping("/pageCpu")
    public  ModelAndView page( @RequestParam Integer id) {

        ModelAndView mav=new ModelAndView("cpu-page");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/getByIdCpu")
                .queryParam("id",id )
                .buildAndExpand().toUri();

        ResponseObject<CpuView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        CpuView view =(CpuView) convertInObject(resp , CpuView.class);
        mav.addObject("cpu", view );
        return mav;
    }
}
