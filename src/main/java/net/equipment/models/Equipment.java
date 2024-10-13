package net.equipment.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.equipment.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "equipment")
public class Equipment {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long equipmentId;
    private String name;
    private String description;
    private String serialNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private EquipmentCategory category;

//    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

//    @OneToOne(mappedBy = "equipment")
//    private QRCode qrCode;
    private String qrCode;

//    @JsonIgnore
    //@ManyToOne
    //@JoinColumn(name = "location_id")
    private String location;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;


}
