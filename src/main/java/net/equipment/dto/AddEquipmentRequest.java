
package net.equipment.dto;

import lombok.*;
import net.equipment.models.Company;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddEquipmentRequest {
    private String name;
    private String description;
    private String serialNumber;
    private Long categoryId;
    private Long companyId;
}
