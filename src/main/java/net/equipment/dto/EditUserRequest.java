package net.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.equipment.models.Company;
import net.equipment.models.Role;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditUserRequest {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
}