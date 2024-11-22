
package net.equipment.repositories;

import java.util.Optional;
import net.equipment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieves a user by their email.
     *
     * @param email the email of the user to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Retrieves a list of users that belong to a specific admin.
     *
     * @param adminId the ID of the admin whose users are to be retrieved
     * @return a list of users associated with the specified admin
     */
    @Query("SELECT u FROM User u WHERE u.company.admin.id = :adminId")
    List<User> findUsersByAdminId(@Param("adminId") Long adminId);

    /**
     * Retrieves a list of users that belong to a specific company.
     *
     * @param companyId the ID of the company whose users are to be retrieved
     * @return a list of users associated with the specified company
     */
    @Query("SELECT u FROM User u WHERE u.company.companyId = :companyId")
    List<User> findUsersByCompanyId(@Param("companyId") Long companyId);

}
