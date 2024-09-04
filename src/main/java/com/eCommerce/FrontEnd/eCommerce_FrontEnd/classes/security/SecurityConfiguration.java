package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception{

        return http.authorizeHttpRequests((request) -> request
                .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .requestMatchers("/user", "/user/**").hasRole("USER")
                .requestMatchers("/**").permitAll())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler()).permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true).permitAll())
                //Ti carica gli utenti dal db.
                .userDetailsService(myUserDetailsService)
                .build();
    }

    @Bean                           //recupera gli utenti in sessione
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        //REQUEST HttpServletRequest E myUserDetailsService VENGONO COLLEGATI AUTOMATICAMENTE DOPO IL SETATTRIBUTE
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            role = URLEncoder.encode(role, StandardCharsets.UTF_8.toString());
            String username = authentication.getName();
            username = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
            myUserDetailsService.httpServletRequest.getSession().setAttribute("username",username);
            myUserDetailsService.httpServletRequest.getSession().setAttribute("role",role);
            response.sendRedirect("/home");
        };
    }
}
