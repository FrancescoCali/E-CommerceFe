package com.eCommerce.FrontEnd.eCommerce_FrontEnd.Configuration;

import com.eCommerce.FrontEnd.eCommerce_FrontEnd.dto.request.UserRequest;
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
public class UserService {

    /**
     * Password encoder used to encode user passwords.
     */
    @Autowired
    private PasswordEncoder getPasswordEncoder;

    /**
     * In-memory user details manager used to manage user details.
     */
    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;

    /**
     * Constructs a {@link UserService} with the given {@link InMemoryUserDetailsManager}.
     *
     * @param inMemoryUserDetailsManager The in-memory user details manager used to manage user details.
     */
    @Autowired
    public UserService(InMemoryUserDetailsManager inMemoryUserDetailsManager) {
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
    public void createUser(UserRequest req) {
        if (!inMemoryUserDetailsManager.userExists(req.getEmail())) {
            log.debug(req.getEmail() + " does not exist, creating new user");

            UserDetails createUser = User.withUsername(req.getEmail())
                    .password(getPasswordEncoder.encode(req.getPassword().toString()))
                    .roles(req.getRole())
                    .build();
            inMemoryUserDetailsManager.createUser(createUser);
            log.debug(req.getEmail() + " created");
        } else {
            log.debug(req.getEmail() + " already exists");
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
    public void updateUser(UserRequest req) {
        if (inMemoryUserDetailsManager.userExists(req.getEmail())) {
            log.debug(req.getEmail() + " exists");

            UserDetails updatedUser = User.withUsername(req.getEmail())
                    .password(getPasswordEncoder.encode(req.getPassword().toString()))
                    .roles(req.getRole())
                    .build();
            inMemoryUserDetailsManager.updateUser(updatedUser);
            log.debug(req.getEmail() + " updated");
        } else {
            log.warn(req.getEmail() + " does not exist and cannot be updated");
        }
    }

    public void removeUser(UserRequest req) {
        if (inMemoryUserDetailsManager.userExists(req.getEmail())) {
            log.debug(req.getEmail() + " exists");

            UserDetails deleteUser = User.withUsername(req.getEmail())
                    .password(getPasswordEncoder.encode(req.getPassword().toString()))
                    .roles(req.getRole())
                    .build();
            inMemoryUserDetailsManager.deleteUser(deleteUser.getUsername());

            log.debug(req.getEmail() + " delete");
        } else {
            log.warn(req.getEmail() + " does not exist and cannot be updated");
        }
    }

}
