package net.equipment.models;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "qr_codes")
public class QRCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    @Lob
    private String imageData; // Храним QR-код как Base64 строку
}
