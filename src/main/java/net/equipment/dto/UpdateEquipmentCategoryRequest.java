package net.equipment.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEquipmentCategoryRequest {
    private String name;
    private Integer expirationPeriodInMonths;
}

