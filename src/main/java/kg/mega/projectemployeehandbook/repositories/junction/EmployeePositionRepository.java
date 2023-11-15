package kg.mega.projectemployeehandbook.repositories.junction;

import kg.mega.projectemployeehandbook.models.entities.junction.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {
}
