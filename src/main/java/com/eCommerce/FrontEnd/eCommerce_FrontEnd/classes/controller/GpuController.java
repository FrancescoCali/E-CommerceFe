package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.GpuRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.CpuView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.GpuView;
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
@RequestMapping("/gpu")
public class GpuController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    public static Logger log = LoggerFactory.getLogger(GpuController.class);


    @GetMapping("/createGpu")
    public ModelAndView create(){
        ModelAndView mav = new ModelAndView("create-gpu");
        GpuRequest req = new GpuRequest();
        req.setErrorMSG(null);
        mav.addObject("gpu", req);

        return mav;
    }

    @PostMapping("/saveGpu")
    public Object save(@ModelAttribute("gpu") GpuRequest req){

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "gpu/create")
                .buildAndExpand()
                .toUri();
        log.debug("uri: "+uri);

        ResponseBase resp = rest.postForEntity(uri,req,ResponseBase.class).getBody();

        log.debug("rc:" + resp.getRc());

        if(!resp.getRc()){
            ModelAndView mav = new ModelAndView("create-gpu");
            req.setErrorMSG(req.getErrorMSG());
            mav.addObject("gpu", req);
            return mav;
        }
        return "redirect:/components/listGpu";
    }

    @GetMapping("/removeGpu")
    public Object remove(@RequestParam Integer id){
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "/gpu/remove")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();

        ResponseBase resp = rest.postForEntity(uri,id,ResponseBase.class).getBody();
        return "redirect:/components/listGpu";
    }

    @GetMapping("/updateGpu")
    public ModelAndView update(@RequestParam Integer id) {
        ModelAndView mav = new ModelAndView("create-update-Gpu");

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "gpu/getById")
                .queryParam("id", id)
                .buildAndExpand()
                .toUri();

        @SuppressWarnings("unchecked")
        ResponseObject<GpuView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        GpuRequest req = (GpuRequest) convertInObject(resp.getDati(),GpuRequest.class);

        mav.addObject("gpu",req);
        mav.addObject("myTitle", "Modifica gpu");
        return mav;
    }

    @GetMapping("/pageGpu")
    public  ModelAndView page( @RequestParam Integer id) {

        ModelAndView mav=new ModelAndView("gpu-page");
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "product/getByIdGpu")
                .queryParam("id",id )
                .buildAndExpand().toUri();

        ResponseObject<GpuView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
        GpuView view =(GpuView) convertInObject(resp , GpuView.class);
        mav.addObject("gpu", view );
        return mav;
    }

}
