
package net.equipment.repositories;

import net.equipment.models.Company;
import net.equipment.models.EquipmentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentCategoryRepository extends JpaRepository<EquipmentCategory, Long> {

    /**
     * Finds all equipment categories associated with a specific admin.
     *
     * @param userId the ID of the admin whose equipment categories are to be retrieved
     * @return a list of equipment categories associated with the specified admin
     */
    List<EquipmentCategory> findByAdminId(Long userId);
}