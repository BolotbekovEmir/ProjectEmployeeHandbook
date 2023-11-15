package kg.mega.projectemployeehandbook.repositories.junction;

import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStatusRepository extends JpaRepository<EmployeeStatus, Long> {}
