
package net.equipment.repositories;

import java.util.List;
import net.equipment.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    /**
     * Finds all companies associated with a specific admin.
     *
     * @param userId the ID of the admin whose companies are to be retrieved
     * @return a list of companies associated with the specified admin
     */
    List<Company> findByAdminId(Long userId);
}
