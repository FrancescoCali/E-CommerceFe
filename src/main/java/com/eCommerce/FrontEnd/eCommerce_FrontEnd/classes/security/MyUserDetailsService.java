package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.security;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Value("${eCommerce.backend}")
    private String backend;
    @Autowired
    private RestTemplate rest;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest httpServletRequest;

    public void setUsername(String username){
        httpServletRequest.getSession().setAttribute( "username" ,username );
    }

    public String getUsername(){
        return (String) httpServletRequest.getSession().getAttribute("username");
    }
    public void setRole(String role){
         httpServletRequest.getSession().setAttribute( "username" ,role );
    }

    public String getRole(){
       return (String)  httpServletRequest.getSession().getAttribute("role");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getByUsername")
                .queryParam("username", username)
                .build()
                .toUri();
        ResponseObject<Map<String, Object>> response = rest.getForEntity(uri, ResponseObject.class).getBody();
        if (response == null || response.getDati() == null) throw new UsernameNotFoundException("User not found");
        Map<String, Object> userData = response.getDati();

        return User.withUsername(userData.get("username").toString())
                .password(passwordEncoder.encode(userData.get("password").toString()))
                .roles(userData.get("role").toString())
                .build();
    }
}
