package net.equipment.services;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import net.equipment.dto.*;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.mapper.UserMapper;
import net.equipment.models.Company;
import net.equipment.models.Role;
import net.equipment.models.User;
import net.equipment.repositories.CompanyRepository;
import net.equipment.repositories.UserRepository;
import net.equipment.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, companyRepository);
    }
    

    @Test
    public void testSave_ExistingUser() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("test@example.com");
        existingUser.setFirstName("John");
        existingUser.setLastName("Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.save(existingUser);

        // Assert
        assertNotNull(savedUser.getUpdatedAt());
        assertEquals("test@example.com", savedUser.getEmail());
    }

    @Test
    public void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        // Act
        List<UserDto> users = userService.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1@example.com", users.get(0).getEmail());
    }

    @Test
    public void testGetUserById() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setEmail("user@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        UserDto userDto = userService.getUserById(userId);

        // Assert
        assertEquals(userId, userDto.getId());
        assertEquals("user@example.com", userDto.getEmail());
    }

    @Test
    public void testGetUserById_NotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
        assertEquals("User with this id does not exist1", thrown.getMessage());
    }

    @Test
    public void testGetUserByEmail() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        UserDto userDto = userService.getUserByEmail(email);

        // Assert
        assertEquals(email, userDto.getEmail());
    }

    @Test
    public void testGetUserByEmail_NotFound() {
        // Arrange
        String email = "user@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserByEmail(email);
        });
        assertEquals("User with this email does not existuser@example.com", thrown.getMessage());
    }

    @Test
    public void testUpdateProfile() {
        // Arrange
        Long userId = 1L;
        EditUserRequest updatedUser = new EditUserRequest();
        updatedUser.setEmail("updated@example.com");
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDto updatedUserDto = userService.updateProfile(userId, updatedUser);

        // Assert
        assertEquals("updated@example.com", updatedUserDto.getEmail());
    }

    @Test
    public void testUpdateUser() {
        // Arrange
        Long userId = 1L;
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setEmail("newemail@example.com");
        updateUserRequest.setFirstName("Updated");
        updateUserRequest.setLastName("User");
        updateUserRequest.setRole(Role.ROLE_USER);
        updateUserRequest.setCompanyId(1L);

        Company company = new Company();
        company.setCompanyId(1L);
        company.setName("Company1");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("oldemail@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserDto updatedUserDto = userService.updateUser(userId, updateUserRequest);

        // Assert
        assertEquals("newemail@example.com", updatedUserDto.getEmail());
        assertEquals("Company1", updatedUserDto.getCompany().getName());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    public void testDeleteUser_NotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            userService.deleteUser(userId);
        });
        assertEquals("User with this id does not exist1", thrown.getMessage());
    }

    @Test
    public void testGetUsersByAdminId() {
        // Arrange
        Long adminId = 1L;
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        when(userRepository.findUsersByAdminId(adminId)).thenReturn(List.of(user1, user2));

        // Act
        List<UserDto> users = userService.getUsersByAdminId(adminId);

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1@example.com", users.get(0).getEmail());
    }

    @Test
    public void testGetUsersByCompanyId() {
        // Arrange
        Long companyId = 1L;
        User user1 = new User();
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setEmail("user2@example.com");

        when(userRepository.findUsersByCompanyId(companyId)).thenReturn(List.of(user1, user2));

        // Act
        List<UserDto> users = userService.getUsersByCompanyId(companyId);

        // Assert
        assertEquals(2, users.size());
        assertEquals("user1@example.com", users.get(0).getEmail());
    }
}
