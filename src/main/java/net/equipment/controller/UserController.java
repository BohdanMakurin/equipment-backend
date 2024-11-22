package net.equipment.controller;

import lombok.AllArgsConstructor;
import net.equipment.dto.CreateUserRequest;
import net.equipment.dto.EditUserRequest;
import net.equipment.dto.UpdateUserRequest;
import net.equipment.dto.UserDto;
import net.equipment.models.Company;
import net.equipment.models.User;
import net.equipment.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * Controller for managing user-related operations.
 * Provides endpoints for creating, retrieving, updating, and deleting users,
 * as well as filtering users by admin and company.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the ID of the user to retrieve
     * @return a ResponseEntity containing the user data (UserDto) and HTTP status 200 (OK)
     */
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email address of the user to retrieve
     * @return a ResponseEntity containing the user data (UserDto) and HTTP status 200 (OK)
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a ResponseEntity containing a list of users (UserDto) and HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Updates the profile of a user by their ID.
     *
     * @param userId the ID of the user to update
     * @param updatedUser the request object containing the updated user profile details
     * @return a ResponseEntity containing the updated user data (UserDto) and HTTP status 200 (OK)
     */
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateProfile(@PathVariable("id") Long userId,
                                                 @RequestBody EditUserRequest updatedUser) {
        UserDto userDto = userService.updateProfile(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Updates a user's information by their ID. This is for worker role updates.
     *
     * @param userId the ID of the user to update
     * @param updatedUser the request object containing the updated user details
     * @return a ResponseEntity containing the updated user data (UserDto) and HTTP status 200 (OK)
     */
    @PutMapping("/workerUpdate/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId,
                                              @RequestBody UpdateUserRequest updatedUser) {
        UserDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId the ID of the user to delete
     * @return a ResponseEntity with a success message and HTTP status 200 (OK)
     */
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    /**
     * Retrieves a list of users managed by a specific admin.
     *
     * @param adminId the ID of the admin
     * @return a ResponseEntity containing a list of users (UserDto) and HTTP status 200 (OK)
     * @throws Exception if an error occurs during the operation
     */
    @GetMapping({"byAdmin/{id}"})
    public ResponseEntity<List<UserDto>> getUsersByAdmin(@PathVariable("id") Long adminId) throws Exception {
        List<UserDto> users = userService.getUsersByAdminId(adminId);
        return ResponseEntity.ok(users);
    }

    /**
     * Retrieves a list of users associated with a specific company.
     *
     * @param companyId the ID of the company
     * @return a ResponseEntity containing a list of users (UserDto) and HTTP status 200 (OK)
     * @throws Exception if an error occurs during the operation
     */
    @GetMapping({"byCompany/{id}"})
    public ResponseEntity<List<UserDto>> getUsersByCompany(@PathVariable("id") Long companyId) throws Exception {
        List<UserDto> users = userService.getUsersByCompanyId(companyId);
        return ResponseEntity.ok(users);
    }
}

