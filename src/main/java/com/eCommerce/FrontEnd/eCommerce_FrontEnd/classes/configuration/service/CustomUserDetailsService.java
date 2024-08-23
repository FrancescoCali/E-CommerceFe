package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.service;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.securityConfig.MyConfig;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Service for managing user details.
 * <p>
 * This class is responsible for loading users from an external source and creating an in-memory
 * management of users with authentication and authorization details.
 * </p>
 *
 * @author FrancescoCali
 * @author MirkoFerrara
 * @author marcoguzzo
 * @author AngeloAnatilopan
 */
@Service
public class CustomUserDetailsService {

    /**
     * URL of the backend configured to obtain user data.
     */
    @Value("${eCommerce.backend}")
    String backend;
    /**
     * RestTemplate for making HTTP calls.
     */
    @Autowired
    RestTemplate rest;
    /**
     * Encoder for user passwords.
     */
    @Autowired
    private PasswordEncoder getPasswordEncoder;
    /**
     * Logger for the class.
     */
    public static Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    /**
     * Loads users from an external source and returns them as an {@link InMemoryUserDetailsManager}.
     * <p>
     * This method makes an HTTP request to the backend to get a list of users, then creates a
     * list of {@link UserDetails} which is used to create an {@link InMemoryUserDetailsManager}.
     * </p>
     *
     * @return An instance of {@link InMemoryUserDetailsManager} containing the user details.
     */
//    public InMemoryUserDetailsManager loadUser(){
//
//        List<UserDetails> userDetailsList = new ArrayList<>();
//
//        URI uri = UriComponentsBuilder
//                .fromHttpUrl(backend + "user/list")
//                .buildAndExpand()
//                .toUri();
//
//        Response<HashMap<String, Object>> resp = rest.getForEntity(uri, Response.class).getBody();
//
//        for (HashMap<String, Object> ut : resp.getDati()) {
//            log.debug("loadUser... " + ut.get("username"));
//
//            userDetailsList.add(
//                    User.withUsername(ut.get("username").toString())
//                            .password(getPasswordEncoder.encode(ut.get("password").toString()))
//                            .roles(ut.get("role").toString())
//                            .build()
//            );
//            System.out.println(resp.getDati());
//        }
//        return new InMemoryUserDetailsManager(userDetailsList);
//    }

    public InMemoryUserDetailsManager loadUser(Integer id){

        List<UserDetails> userDetailsList = new ArrayList<>();
        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/getById")
                .queryParam("id",id)
                .buildAndExpand()
                .toUri();
        Response<HashMap<String, Object>> resp = rest.getForEntity(uri, Response.class).getBody();
        for (HashMap<String, Object> ut : resp.getDati()) {
            userDetailsList.add(
                    User.withUsername(ut.get("username").toString())
                            .password(getPasswordEncoder.encode(ut.get("password").toString()))
                            .roles(ut.get("role").toString())
                            .build()
            ); }
        return new InMemoryUserDetailsManager(userDetailsList);
    }
}