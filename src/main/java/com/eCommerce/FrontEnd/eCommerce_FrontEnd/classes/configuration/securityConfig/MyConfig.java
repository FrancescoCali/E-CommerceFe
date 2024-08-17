package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for defining beans related to security.
 * <p>
 * This class provides a Spring configuration that defines a bean for password encoding
 * using the BCrypt algorithm.
 * </p>
 *
 * @author FrancescoCali
 * @author MirkoFerrara
 * @author marcoguzzo
 * @author AngeloA03
 */
@Configuration
public class MyConfig {

    /**
     * Provides a {@link PasswordEncoder} bean configured with BCrypt encoding.
     * <p>
     * This method creates and returns a {@link BCryptPasswordEncoder} instance that is
     * used for encoding passwords securely using the BCrypt hashing algorithm.
     * </p>
     *
     * @return An instance of {@link PasswordEncoder} configured with BCrypt encoding.
     */
    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
