package com.eCommerce.FrontEnd.eCommerce_FrontEnd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
public class MonitorController {
    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;
}
