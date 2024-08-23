package com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.service;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.configuration.securityConfig.InMemorySecurityConfig;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.classes.dto.request.UserRequest;
import com.eCommerce.FrontEnd.eCommerce_FrontEnd.interfaces.iService.iUserService;
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


    /**
     * Password encoder used to encode user passwords.
     */
    @Autowired
    private PasswordEncoder getPasswordEncoder;

    /**
     * In-memory user details manager used to manage user details.
     */
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final  InMemorySecurityConfig inMemorySecurityConfig ;
    /**
     * Constructs a {@link UserService} with the given {@link InMemoryUserDetailsManager}.
     *
     * @param inMemoryUserDetailsManager The in-memory user details manager used to manage user details.
     */
    @Autowired
    public UserService(InMemoryUserDetailsManager inMemoryUserDetailsManager,InMemorySecurityConfig inMemorySecurityConfig ) {
        this.inMemorySecurityConfig = inMemorySecurityConfig;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

    /**
     * Logger for the class.
     */
    private static Logger log = LoggerFactory.getLogger(UserService.class);

    /**
     * Creates a new user.
     * <p>
     * This method checks if the user already exists in the {@link InMemoryUserDetailsManager}. If the user does
     * not exist, it creates a new user with the details provided in the {@link UserRequest}. If the user already
     * exists, it logs a debug message indicating the user's existence.
     * </p>
     *
     * @param req The {@link UserRequest} containing the details of the user to be created.
     */
    @Override
    public void createUser(UserRequest req) {
        if (!inMemoryUserDetailsManager.userExists(req.getUsername())) {
            log.debug(req.getUsername() + " does not exist, creating new user");
            UserDetails createUser = User.withUsername(req.getUsername())
                    .password(getPasswordEncoder.encode(req.getPassword().toString()))
                    .roles(req.getRole())
                    .build();

            inMemoryUserDetailsManager.createUser(createUser);
            inMemorySecurityConfig.inMemory();
        } else {
            log.debug(req.getUsername() + " already exists");
        }
    }
    /**
     * Updates the details of an existing user.
     * <p>
     * This method checks if the user exists in the {@link InMemoryUserDetailsManager}. If the user exists,
     * it updates the user's details with the provided {@link UserRequest}. If the user does not exist,
     * a warning message is logged.
     * </p>
     *
     * @param req The {@link UserRequest} containing the user details to be updated.
     */
    @Override
    public void updateUser(UserRequest req) {
        if (inMemoryUserDetailsManager.userExists(req.getUsername())) {
            log.debug(req.getUsername() + " exists");

            UserDetails updatedUser = User.withUsername(req.getUsername())
                    .password(getPasswordEncoder.encode(req.getPassword().toString()))
                    .roles(req.getRole())
                    .build();
            inMemoryUserDetailsManager.updateUser(updatedUser);
            inMemorySecurityConfig.inMemory();
            log.debug(req.getUsername() + " updated");
        } else
            log.warn(req.getUsername() + " does not exist and cannot be updated");

    }
    /**
     * Removes an existing user.
     * <p>
     * This method checks if the user exists in the {@link InMemoryUserDetailsManager}. If the user exists,
     * it removes the user based on the provided {@link UserRequest}. If the user does not exist,
     * a warning message is logged indicating that the user could not be removed.
     * </p>
     *
     * @param req The {@link UserRequest} containing the details of the user to be removed.
     */
    @Override
    public void removeUser(UserRequest req) {
        if (inMemoryUserDetailsManager.userExists(req.getUsername())) {
            log.debug(req.getUsername() + " exists");

            UserDetails deleteUser = User.withUsername(req.getUsername())
                    .password(getPasswordEncoder.encode(req.getPassword().toString()))
                    .roles(req.getRole())
                    .build();

            inMemoryUserDetailsManager.deleteUser(deleteUser.getUsername());
            inMemorySecurityConfig.inMemory();
            log.debug(req.getUsername() + " delete");
        } else {
            log.warn(req.getUsername() + " does not exist and cannot be removed");
        }
    }

}
