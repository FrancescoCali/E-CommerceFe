package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.controller;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
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
public class HomeController {

    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;

    @GetMapping("/home")
    public ModelAndView homepage(@RequestParam(required=false)String username){
        URI uri ;
        UserView req = null ;
        ModelAndView mav;
        if(username!=null) {
            uri = UriComponentsBuilder
                    .fromHttpUrl(backend + "user/getByUsername")
                    .queryParam("username", username)
                    .buildAndExpand().toUri();

            ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
            req = (UserView) convertInObject(resp.getDati(), UserView.class);

            if (req.getRole().equalsIgnoreCase("ADMIN"))
                mav = new ModelAndView("home-admin");
            else
                mav = new ModelAndView("home-user");

            mav.addObject("role", req.getRole());
        } else
            mav = new ModelAndView("home-user");

        return mav;
    }
//    @Controller
//    public class HomeController {
//
//        @Value("${eCommerce.backend}")
//        String backend;
//
//        @Autowired
//        RestTemplate rest;
//
//        @GetMapping("/home")
//        public ModelAndView homepage(@RequestParam(required=false) String username) {
//            URI uri;
//            UserRequest req = null;
//            ModelAndView mav;
//
//            if (username != null) {
//                uri = UriComponentsBuilder
//                        .fromHttpUrl(backend + "user/getByUsername")
//                        .queryParam("username", username)
//                        .buildAndExpand().toUri();
//
//                ResponseEntity<ResponseObject<UserRequest>> responseEntity = rest.exchange(
//                        uri,
//                        HttpMethod.GET,
//                        null,
//                        new ParameterizedTypeReference<ResponseObject<UserRequest>>() {}
//                );
//
//                ResponseObject<UserRequest> resp = responseEntity.getBody();
//                req = resp.getDati();
//
//                if (req.getRole().equalsIgnoreCase("ADMIN"))
//                    mav = new ModelAndView("home-admin");
//                else
//                    mav = new ModelAndView("home-user");
//
//                mav.addObject("role", req.getRole());
//            } else {
//                mav = new ModelAndView("home-user");
//            }
//
//            return mav;
//        }
}
