package net.equipment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.Name;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Company {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    @Column(name = "company_id")
    Long companyId;
    @ManyToOne
    @JoinColumn(name = "id")
    User admin;
    @NotNull(message = "Name cannot be null")
    @Size(min = 1, message = "Name must have at least 1 character")
    String name;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @JsonManagedReference
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = false)
    List<User> employees;

}
