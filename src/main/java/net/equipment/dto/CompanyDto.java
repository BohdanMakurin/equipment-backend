
package net.equipment.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;
import net.equipment.models.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {
    private Long companyId;
    private User admin;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<User> employees;
}
