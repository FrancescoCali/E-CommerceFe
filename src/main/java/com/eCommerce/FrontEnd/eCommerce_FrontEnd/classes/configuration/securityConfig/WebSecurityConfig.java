package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.securityConfig;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.service.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

///**
// * Security configuration class for setting up web security.
// * <p>
// * This class configures web security for the application, including URL access rules,
// * form login, and logout settings.
// * </p>
// *
// * @author FrancescoCali
// * @author MirkoFerrara
// * @author marcoguzzo
// * @author AngeloA03
// */
//
//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//
//    @Value("${eCommerce.backend}")
//    String backend;
//
//    @Autowired
//    RestTemplate rest;
//
//    public static Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    /**
//     * Configures the {@link SecurityFilterChain} for the application.
//     * <p>
//     * This method defines security rules for HTTP requests, such as URL patterns that require specific roles,
//     * form login configuration, and logout settings. It builds and returns a {@link SecurityFilterChain}
//     * instance with the configured settings.
//     * </p>
//     *
//     * @param http An instance of {@link HttpSecurity} to configure security settings.
//     * @return A configured {@link SecurityFilterChain} instance.
//     * @throws Exception If an error occurs during the configuration.
//     */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((request) -> request
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/user/createUser", "/user/saveUser").permitAll()
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .successHandler(authenticationSuccessHandler())
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                );
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//
//        // customUserDetailsService.loadUser(username);
//
//        return new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                                Authentication authentication) throws IOException, ServletException {
//
//                String username = authentication.getName();
//
//                URI uri = UriComponentsBuilder
//                            .fromHttpUrl(backend + "user/getByUsername")
//                            .queryParam("username", username)
//                            .buildAndExpand().toUri();
//
//                ResponseObject<UserView> resp = rest.getForEntity(uri, ResponseObject.class).getBody();
//                UserView req = (UserView) convertInObject(resp.getDati(), UserView.class);
//
//                response.sendRedirect("/home?role=" + req.getRole());
//            }
//        };
//    }
//}

//@Configuration
//@EnableWebSecurity
//public class WebSecurityConfig {
//
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((request) -> request
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/user/createUser", "/user/saveUser").permitAll()
//                        .requestMatchers("/user/**").hasRole("USER")
//                        .requestMatchers("/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .successHandler(authenticationSuccessHandler())
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout")
//                        .invalidateHttpSession(true)
//                        .deleteCookies("JSESSIONID")
//                        .permitAll()
//                )
//                .userDetailsService(customUserDetailsService);
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationSuccessHandler authenticationSuccessHandler() {
//        return new AuthenticationSuccessHandler() {
//            @Override
//            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                                Authentication authentication) throws IOException, ServletException {
//
//                // Estrai il ruolo senza le parentesi
//                String role = authentication.getAuthorities().iterator().next().getAuthority();
//                // Codifica l'URL
//                role = URLEncoder.encode(role, StandardCharsets.UTF_8.toString());
//                // Estrai lo username senza le parentesi
//                String username = authentication.getName();
//
//                // Codifica l'URL
//                username = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
//
//                // Reindirizza a /home con il parametro codificato
//                response.sendRedirect("/home?username=" + username +"&role="+role );
//            }
//        };
//    }
// }

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

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
                )
                .userDetailsService(customUserDetailsService);

        return http.build();
    }

    @Bean                           //recupera gli utenti in sessione
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                String role = authentication.getAuthorities().iterator().next().getAuthority();
                role = URLEncoder.encode(role, StandardCharsets.UTF_8.toString());
                String username = authentication.getName();
                username = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
                request.getSession().setAttribute("username",username);
                request.getSession().setAttribute("role",role);

                response.sendRedirect("/home");
            }
        };
    }
}




