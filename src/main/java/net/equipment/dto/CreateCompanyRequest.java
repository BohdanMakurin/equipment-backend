
package net.equipment.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCompanyRequest {
    private Long adminId;
    private String name;
    private String description;
}
