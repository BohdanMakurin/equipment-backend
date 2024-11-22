package net.equipment.services;

import net.equipment.dto.*;
import net.equipment.mapper.UserMapper;
import net.equipment.models.Role;
import net.equipment.repositories.CompanyRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import net.equipment.repositories.UserRepository;
import net.equipment.models.User;
import net.equipment.services.JwtService;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

/**
 * Service class for managing user authentication and registration.
 * Handles signup, signin, and user creation with role assignments.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new admin user and generates a JWT token for the session.
     *
     * @param request the SignUpRequest object containing user registration details
     * @return a JwtAuthenticationResponse containing the JWT token
     */
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();

        user = userService.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    /**
     * Authenticates a user by their email and password, and generates a JWT token upon successful login.
     *
     * @param request the SignInRequest object containing the login credentials
     * @return a JwtAuthenticationResponse containing the JWT token
     * @throws IllegalArgumentException if the provided email or password is incorrect
     */
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    /**
     * Creates a new user with the specified details and assigns them to a company and role.
     *
     * @param request the CreateUserRequest object containing the new user's details
     * @return a UserDto object representing the created user
     */
    public UserDto createUser(CreateUserRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .company(companyService.getCompanyById(request.getCompanyId()))
                .role(request.getRole())
                .build();

        user = userService.save(user);
        return UserMapper.mapToUserDto(user);
    }
}
