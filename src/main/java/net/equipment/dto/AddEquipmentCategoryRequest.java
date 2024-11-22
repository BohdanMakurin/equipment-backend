package net.equipment.dto;

import lombok.*;
import net.equipment.models.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddEquipmentCategoryRequest {
    private String name;
    private Long adminId;
    private Integer expirationPeriodInMonths;
}
