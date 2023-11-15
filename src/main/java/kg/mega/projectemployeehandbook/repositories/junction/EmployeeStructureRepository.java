package kg.mega.projectemployeehandbook.repositories.junction;

import kg.mega.projectemployeehandbook.models.entities.junction.EmployeeStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeStructureRepository extends JpaRepository<EmployeeStructure, Long> {
}
