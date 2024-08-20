//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.controller;

import java.util.List;

import lombok.AllArgsConstructor;
import net.equipment.dto.UserDto;
import net.equipment.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping({"/api/v1/test"})
public class TestController {
    private final UserService userService;

    @GetMapping({"/anon"})
    public String anonEndPoint() {
        return "everyone can see this";
    }

    @GetMapping({"/users"})
    @PreAuthorize("hasRole('USER')")
    public String usersEndPoint() {
        return "ONLY users can see this";
    }

    @GetMapping({"/admins"})
    @PreAuthorize("hasRole('ADMIN')")
    public String adminsEndPoint() {
        return "ONLY admins can see this";
    }

    @GetMapping({"/managers"})
    @PreAuthorize("hasRole('MANAGER')")
    public String managersEndPoint() {
        return "ONLY MANAGER can see this";
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> users = this.userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
