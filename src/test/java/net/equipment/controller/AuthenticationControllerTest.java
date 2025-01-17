package net.equipment.controller;

import net.equipment.dto.*;
import net.equipment.models.Role;
import net.equipment.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignUpRequest signUpRequest;
    private SignInRequest signInRequest;
    private CreateUserRequest createUserRequest;

    @BeforeEach
    public void setup() {
        // Initialize request objects with test data
        signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setFirstName("John");
        signUpRequest.setLastName("Doe");

        signInRequest = new SignInRequest();
        signInRequest.setEmail("test@example.com");
        signInRequest.setPassword("password123");

        createUserRequest = new CreateUserRequest();
        createUserRequest.setEmail("newuser@example.com");
        createUserRequest.setFirstName("New");
        createUserRequest.setLastName("User");
        createUserRequest.setRole(Role.ROLE_USER);
    }

    @Test
    public void signupTest() throws Exception {
        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse("test-token");

        // Mock the response from authenticationService for /signup
        when(authenticationService.signup(any(SignUpRequest.class)))
                .thenReturn(jwtResponse);

        // Perform a POST request to /signup and check the status and response body
        mockMvc.perform(post("/api/v1/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("test-token")));
    }

    @Test
    public void signinTest() throws Exception {
        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse("signin-token");

        // Mock the response from authenticationService for /signin
        when(authenticationService.signin(any(SignInRequest.class)))
                .thenReturn(jwtResponse);

        // Perform a POST request to /signin and check the status and response body
        mockMvc.perform(post("/api/v1/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("signin-token")));
    }

    @Test
    @WithMockUser
    void testCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();

        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("username");
        userDto.setEmail("email@example.com");

        when(authenticationService.createUser(any(CreateUserRequest.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("username"))
                .andExpect(jsonPath("$.email").value("email@example.com"));

        verify(authenticationService, times(1)).createUser(any(CreateUserRequest.class));
    }
}

