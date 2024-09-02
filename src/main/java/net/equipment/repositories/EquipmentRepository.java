package net.equipment.repositories;

import net.equipment.models.Company;
import net.equipment.models.Equipment;
import net.equipment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
    @Query("SELECT e FROM Equipment e WHERE e.company.admin.id = :adminId")
    List<Equipment> findByAdminId(@Param("adminId") Long adminId);
}
