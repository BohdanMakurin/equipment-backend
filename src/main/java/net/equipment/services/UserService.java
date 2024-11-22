
package net.equipment.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import net.equipment.dto.*;
import net.equipment.exceptions.ResourceNotFoundException;
import net.equipment.mapper.UserMapper;
import net.equipment.models.Company;
import net.equipment.models.User;
import net.equipment.repositories.CompanyRepository;
import net.equipment.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class that provides business logic related to user management.
 * This includes operations such as saving, updating, deleting, and retrieving users.
 */
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    /**
     * Provides a UserDetailsService to load a user by username (email).
     *
     * @return UserDetailsService implementation to load user details by email
     */
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    /**
     * Saves a new or updated user to the repository.
     * If the user is new (id is null), the created date will be set.
     *
     * @param newUser the user object to be saved
     * @return the saved user object with updated information (e.g., created/updated timestamps)
     */
    public User save(User newUser) {
        if (newUser.getId() == null) {
            newUser.setCreatedAt(LocalDateTime.now());  // Set creation timestamp for new users
        }
        newUser.setUpdatedAt(LocalDateTime.now());  // Update timestamp
        User savedUser = userRepository.save(newUser);
        return userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + savedUser.getId()));
    }

    /**
     * Retrieves all users from the repository.
     *
     * @return a list of UserDto objects representing all users
     */
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserDto object corresponding to the user
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));
        return UserMapper.mapToUserDto(user);
    }

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to retrieve
     * @return the UserDto object corresponding to the user
     * @throws ResourceNotFoundException if no user is found with the given email
     */
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with this email does not exist" + email));
        return UserMapper.mapToUserDto(user);
    }

    /**
     * Updates the profile of a user (email, first name, last name).
     *
     * @param userId the ID of the user to update
     * @param updatedUser the request object containing the new user details
     * @return the updated UserDto object
     * @throws ResourceNotFoundException if the user with the given ID is not found
     */
    public UserDto updateProfile(Long userId, EditUserRequest updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));

        user.setEmail(updatedUser.getEmail());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    /**
     * Updates a user's information, including company, email, role, and personal details.
     *
     * @param userId the ID of the user to update
     * @param updatedUser the request object containing the new user details
     * @return the updated UserDto object
     * @throws ResourceNotFoundException if the user or company (if provided) is not found
     */
    public UserDto updateUser(Long userId, UpdateUserRequest updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));

        if (updatedUser.getCompanyId() != null) {
            Company newCompany = companyRepository.findById(updatedUser.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist: " + updatedUser.getCompanyId()));
            user.setCompany(newCompany);
        }
        user.setEmail(updatedUser.getEmail());
        user.setRole(updatedUser.getRole());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));
        userRepository.deleteById(userId);
    }

    /**
     * Retrieves users that belong to a specific admin based on the admin's ID.
     *
     * @param adminId the ID of the admin
     * @return a list of UserDto objects representing users under the admin
     */
    public List<UserDto> getUsersByAdminId(Long adminId) {
        List<User> users = userRepository.findUsersByAdminId(adminId);
        return users.stream().map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves users that belong to a specific company based on the company's ID.
     *
     * @param companyId the ID of the company
     * @return a list of UserDto objects representing users under the company
     */
    public List<UserDto> getUsersByCompanyId(Long companyId) {
        List<User> users = userRepository.findUsersByCompanyId(companyId);
        return users.stream().map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}