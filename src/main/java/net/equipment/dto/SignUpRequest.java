
package net.equipment.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    String firstName;
    String lastName;
    String email;
    String password;
}
