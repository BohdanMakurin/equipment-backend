//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.dto;

import lombok.*;
import net.equipment.models.Company;
import net.equipment.models.EquipmentCategory;
import net.equipment.models.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDto {
    private Long equipmentId;
    private String name;
    private String description;
    private String serialNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private EquipmentCategory category;
    private User user;
    private String location;
    private Company company;

}
