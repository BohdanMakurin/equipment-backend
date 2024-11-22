
package net.equipment.controller;

import lombok.AllArgsConstructor;
import net.equipment.dto.*;
import net.equipment.models.User;
import net.equipment.services.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication and user management operations.
 * Processes requests for user registration, login, and user creation.
 */
@AllArgsConstructor
@RestController
@RequestMapping({"/api/v1"})
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    /**
     * Registers a new user.
     *
     * @param request the registration request object containing user details
     * @return a response with a JWT token for the new user
     */
    @PostMapping({"/signup"})
    public JwtAuthenticationResponse signup(@RequestBody SignUpRequest request) {
        return this.authenticationService.signup(request);
    }

    /**
     * Authenticates an existing user.
     *
     * @param request the login request object containing user credentials
     * @return a response with a JWT token for the authenticated user
     */
    @PostMapping({"/signin"})
    public JwtAuthenticationResponse signin(@RequestBody SignInRequest request) {
        return this.authenticationService.signin(request);
    }

    /**
     * Creates a new user.
     *
     * @param request the user creation request object containing user details
     * @return a response with the details of the created user
     */
    @PostMapping({"/create"})
    public UserDto createUser(@RequestBody CreateUserRequest request) {
        return this.authenticationService.createUser(request);
    }
}
