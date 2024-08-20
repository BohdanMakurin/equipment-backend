package net.equipment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "equipment")
    private QRCode qrCode;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}
