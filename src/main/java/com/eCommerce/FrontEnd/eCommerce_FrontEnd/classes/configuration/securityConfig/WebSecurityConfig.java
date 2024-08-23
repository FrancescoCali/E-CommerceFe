package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.securityConfig;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.view.UserView;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.response.ResponseObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

import static com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.utilities.WebUtils.convertInObject;

/**
 * Security configuration class for setting up web security.
 * <p>
 * This class configures web security for the application, including URL access rules,
 * form login, and logout settings.
 * </p>
 *
 * @author FrancescoCali
 * @author MirkoFerrara
 * @author marcoguzzo
 * @author AngeloA03
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {


    @Value("${eCommerce.backend}")
    String backend;

    @Autowired
    RestTemplate rest;
    /**
     * Logger for the class.
     */
    public static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    /**
     * Configures the {@link SecurityFilterChain} for the application.
     * <p>
     * This method defines security rules for HTTP requests, such as URL patterns that require specific roles,
     * form login configuration, and logout settings. It builds and returns a {@link SecurityFilterChain}
     * instance with the configured settings.
     * </p>
     *
     * @param http An instance of {@link HttpSecurity} to configure security settings.
     * @return A configured {@link SecurityFilterChain} instance.
     * @throws Exception If an error occurs during the configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/createUser", "/user/saveUser").permitAll()
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String username = authentication.getName();

                URI uri = UriComponentsBuilder
                            .fromHttpUrl(backend + "user/getByUsername")
                            .queryParam("username", username)
                            .buildAndExpand().toUri();

                ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
                UserView req = (UserView) convertInObject(resp.getDati(), UserView.class);

                response.sendRedirect("/home?role=" + req.getRole());
            }
        };
    }
}


