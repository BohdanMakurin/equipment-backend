package net.equipment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEquipmentRequest {
    String name;
    String description;
    String serialNumber;
    Long categoryId;
    Long userId;
    Long companyId;
    String location;

}
