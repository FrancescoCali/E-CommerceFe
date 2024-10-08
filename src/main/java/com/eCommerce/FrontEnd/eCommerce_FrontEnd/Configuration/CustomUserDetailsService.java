package com.eCommerce.FrontEnd.eCommerce_FrontEnd.Configuration;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.response.Response;
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
import java.util.Objects;

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
 * @author AngeloA03
 */
@Service
public class CustomUserDetailsService {

    /**
     * URL of the backend configured to obtain user data.
     */
    @Value("${jpa.backend}")
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
    public InMemoryUserDetailsManager loadUser(){
        log.debug("User load");
        List<UserDetails> userDetailsList = new ArrayList<>();

        URI uri = UriComponentsBuilder
                .fromHttpUrl(backend + "user/list")
                .buildAndExpand()
                .toUri();

        Response<HashMap<String, Objects>> resp = rest.getForEntity(uri, Response.class).getBody();

        for (HashMap<String, Objects> ut : resp.getDati()) {
            userDetailsList.add(
                    User.withUsername(ut.get("userName").toString())
                            .password(getPasswordEncoder.encode(ut.get("pwd").toString()))
                            .roles(ut.get("role").toString())
                            .build()
            );
        }
        return new InMemoryUserDetailsManager(userDetailsList);
    }
}
