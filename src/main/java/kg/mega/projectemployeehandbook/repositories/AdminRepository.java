package kg.mega.projectemployeehandbook.repositories;

import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByAdminName(String adminName);

    Optional<Admin> findByPersonalNumber(String personalNumber);

    Set<Admin> findAllByAdminNameContainsIgnoreCase(String adminName);

    Set<Admin> findAllByPersonalNumber(String personalNumber);

    Set<Admin> findAllByAdminRole(AdminRole adminRole);

}
