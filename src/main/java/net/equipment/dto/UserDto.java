//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import net.equipment.models.Company;
import net.equipment.models.Equipment;
import net.equipment.models.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Role role;
    private Company company;
    private List<Equipment> equipment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
