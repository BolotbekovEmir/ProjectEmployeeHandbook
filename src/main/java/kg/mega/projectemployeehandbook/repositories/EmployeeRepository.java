package kg.mega.projectemployeehandbook.repositories;

import kg.mega.projectemployeehandbook.models.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByPersonalNumber(String personalNumber);

    Optional<Employee> findByPhone(String phone);

    Optional<Employee> findByEmail(String email);

}
