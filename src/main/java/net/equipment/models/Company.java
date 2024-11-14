package net.equipment.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;
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
    @Name("company_id")
    Long companyId;
    @ManyToOne
    @JoinColumn(
            name = "id"
    )
    User admin;
    String name;
    String description;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    @JsonManagedReference
    @OneToMany( mappedBy = "company")
    List<User> employees;

}
