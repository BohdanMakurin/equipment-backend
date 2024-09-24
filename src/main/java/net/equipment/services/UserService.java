//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public User save(User newUser) {
        if (newUser.getId() == null) {
            newUser.setCreatedAt(LocalDateTime.now());
        }

        newUser.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(newUser);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));
        return UserMapper.mapToUserDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with this email does not exist" + email));
        return UserMapper.mapToUserDto(user);
    }
    public UserDto updateProfile(Long userId, EditUserRequest updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));

        user.setEmail(updatedUser.getEmail());
//        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//        user.setPassword(updatedUser.getPassword());
//        user.setRole(updatedUser.getRole());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
//        user.setCompany(updatedUser.getCompany());
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));

        if (updatedUser.getCompanyId() != null) {
            Company newCompany = companyRepository.findById(updatedUser.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Company with this id does not exist: " + updatedUser.getCompanyId()));
            user.setCompany(newCompany);
        }
        user.setEmail(updatedUser.getEmail());
//        user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
//        user.setPassword(updatedUser.getPassword());
        user.setRole(updatedUser.getRole());
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserDto(savedUser);
    }
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id does not exist" + userId));
        userRepository.deleteById(userId);
    }

    public List<UserDto> getUsersByAdminId(Long adminId) {
        List<User> users = userRepository.findUsersByAdminId(adminId);
        return users.stream().map((user) -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }

    public List<UserDto> getUsersByCompanyId(Long companyId) {
        List<User> users = userRepository.findUsersByCompanyId(companyId);
        return users.stream().map((user) -> UserMapper.mapToUserDto(user))
                .collect(Collectors.toList());
    }


}
