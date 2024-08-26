package net.equipment.controller;

import lombok.AllArgsConstructor;
import net.equipment.dto.CreateUserRequest;
import net.equipment.dto.EditUserRequest;
import net.equipment.dto.UserDto;
import net.equipment.models.Company;
import net.equipment.models.User;
import net.equipment.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;

    // add user

//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest newUser){
//        User savedUser = userService.createUser(newUser);
//        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
//    }

    // get user by id

    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long userId){
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email){
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    //get allUsers

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }
    //update user

    @PutMapping("{id}")
    public  ResponseEntity<UserDto> updateUser(@PathVariable("id") Long userId,
                                               @RequestBody EditUserRequest updatedUser){
        UserDto userDto = userService.updateUser(userId, updatedUser);
        return ResponseEntity.ok(userDto);
    }

    //delete user

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping({"byAdmin/{id}"})
    public ResponseEntity<List<UserDto>> getUsersByAdmin(@PathVariable("id") Long adminId) throws Exception {
        List<UserDto> users = this.userService.getUsersByAdminId(adminId);
        return ResponseEntity.ok(users);
    }
}
