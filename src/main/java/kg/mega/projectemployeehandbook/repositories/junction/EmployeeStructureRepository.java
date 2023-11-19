package kg.mega.projectemployeehandbook.repositories.junction;

import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.entities.Structure;
import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeStructureRepository extends JpaRepository<EmployeeStructure, Long> {
    Set<EmployeeStructure> findAllByStructure(Structure structure);
    Set<EmployeeStructure> findAllByEmployeeAndEndDateIsNull(Employee employee);
    Optional<EmployeeStructure> findByEmployee_PersonalNumberAndStructureId(String personalNumber, Long structureId);
}
