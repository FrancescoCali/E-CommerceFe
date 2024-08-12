package com.eCommerce.FrontEnd.eCommerce_FrontEnd.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configuration class for in-memory security setup.
 * <p>
 * This class defines a Spring configuration that sets up an in-memory user details manager
 * using a custom user details service.
 * </p>
 *
 * @author FrancescoCali
 * @author MirkoFerrara
 * @author marcoguzzo
 * @author AngeloA03
 */
@Configuration
public class InMemorySecurityConfig {

    /**
     * Custom user details service used to load user details.
     */
    @Autowired
    CustomUserDetailsService customUserDetailsService;

    /**
     * Provides an {@link InMemoryUserDetailsManager} bean configured with user details from
     * {@link CustomUserDetailsService}.
     * <p>
     * This method creates and returns an {@link InMemoryUserDetailsManager} instance
     * that is populated with user details loaded from the custom user details service.
     * </p>
     *
     * @return An instance of {@link InMemoryUserDetailsManager} configured with user details.
     */
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        return customUserDetailsService.loadUser();
    }
}
