package kg.mega.projectemployeehandbook.repositories.junction;

import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.Position;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeePositionRepository extends JpaRepository<EmployeePosition, Long> {
    Set<EmployeePosition> findAllByPosition(Position position);
    Set<EmployeePosition> findAllByEmployeeAndEndDateIsNull(Employee employee);
    Optional<EmployeePosition> findByEmployee_PersonalNumberAndPositionId(String personalNumber, Long positionId);
}
