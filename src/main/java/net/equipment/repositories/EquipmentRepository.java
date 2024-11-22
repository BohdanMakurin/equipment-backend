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

    /**
     * Retrieves a list of equipment associated with a specific admin.
     *
     * @param adminId the ID of the admin whose equipment is to be retrieved
     * @return a list of equipment associated with the specified admin
     */
    @Query("SELECT e FROM Equipment e WHERE e.company.admin.id = :adminId")
    List<Equipment> findByAdminId(@Param("adminId") Long adminId);

    /**
     * Retrieves a list of equipment assigned to a specific user.
     *
     * @param userId the ID of the user whose equipment is to be retrieved
     * @return a list of equipment assigned to the specified user
     */
    @Query("SELECT e FROM Equipment e WHERE e.user.id = :userId")
    List<Equipment> findByUserId(@Param("userId") Long userId);

    /**
     * Retrieves a list of equipment belonging to a specific company.
     *
     * @param companyId the ID of the company whose equipment is to be retrieved
     * @return a list of equipment associated with the specified company
     */
    @Query("SELECT e FROM Equipment e WHERE e.company.companyId = :companyId")
    List<Equipment> findByCompanyId(@Param("companyId") Long companyId);

    /**
     * Retrieves a list of equipment by category ID and company ID.
     *
     * @param categoryId the ID of the equipment category to filter by
     * @param companyId the ID of the company to filter by
     * @return a list of {@link Equipment} that belongs to the specified category and company
     */
    @Query("SELECT e FROM Equipment e WHERE e.category.categoryId = :categoryId AND e.company.companyId = :companyId")
    List<Equipment> findByCategoryIdAndCompanyId(@Param("categoryId") Long categoryId, @Param("companyId") Long companyId);
}
