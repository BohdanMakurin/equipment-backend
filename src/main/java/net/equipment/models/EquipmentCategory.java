package net.equipment.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "equipment_category")
public class EquipmentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    private Integer expirationPeriodInMonths;
}
