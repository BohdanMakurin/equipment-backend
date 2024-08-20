//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.equipment.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddEquipmentRequest {
    private String name;
    private String description;
    private String serialNumber;
    private Long categoryId;
}
