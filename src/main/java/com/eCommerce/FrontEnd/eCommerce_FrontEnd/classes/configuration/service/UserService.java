package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.service;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * Service for managing user operations.
 * <p>
 * This class provides methods for updating and creating user details in an in-memory user details manager.
 * It uses {@link PasswordEncoder} for encoding passwords and {@link InMemoryUserDetailsManager}
 * for managing user details.
 * </p>
 *
 * @author FrancescoCali
 * @author MirkoFerrara
 * @author marcoguzzo
 * @author AngeloAnatilopan
 */

@Service
public class UserService implements iUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;

    private static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private HttpServletRequest request;

    public String getUsername() {
        return (String) request.getSession().getAttribute("username");
    }

    public String getRole() {
        return (String) request.getSession().getAttribute("role");
    }
    public void setUsername(String username){
        request.getSession().setAttribute("username",username);
    }
    public void setRole(String role){
        request.getSession().setAttribute("role",role);
    }
    @Override
    public void createUser(UserRequest req) {

        System.out.println("CREATE USER "+req.getUsername());
        System.out.println("CREATE USER "+req.getRole());

        if (!inMemoryUserDetailsManager.userExists(req.getUsername())) {
            UserDetails createUser = User.withUsername(req.getUsername())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .roles(req.getRole())
                    .build();
                    inMemoryUserDetailsManager.createUser(createUser);
            try {
                setUsername(req.getUsername());
                setRole(req.getRole());
                request.login(req.getUsername(), req.getPassword());
            } catch (ServletException e) {
                log.error("Errore durante l'autenticazione automatica", e);
            }
        } else
            log.debug(req.getUsername() + " already exists");
    }

    @Override
    public void updateUser(UserRequest req) {
        if (inMemoryUserDetailsManager.userExists(req.getUsername())) {
            UserDetails updatedUser = User.withUsername(req.getUsername())
                    .password(passwordEncoder.encode(req.getPassword()))
                    .roles(req.getRole())
                    .build();
            inMemoryUserDetailsManager.updateUser(updatedUser);
        } else
            log.warn(req.getUsername() + " does not exist and cannot be updated");
    }

    @Override
    public void removeUser(UserRequest req) {
        if (inMemoryUserDetailsManager.userExists(req.getUsername())) {
            inMemoryUserDetailsManager.deleteUser(req.getUsername());
        } else
            log.warn(req.getUsername() + " does not exist and cannot be removed");
    }
}

