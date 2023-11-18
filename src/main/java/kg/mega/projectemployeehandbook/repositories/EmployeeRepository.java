package kg.mega.projectemployeehandbook.repositories;

import kg.mega.projectemployeehandbook.models.entities.Employee;
import kg.mega.projectemployeehandbook.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByPersonalNumber(String personalNumber);

    Optional<Employee> findByPhone(String phone);

    Optional<Employee> findByEmail(String email);

    Set<Employee> findAllByStatus(Status status);

    Set<Employee> findAllByFirstnameContainsIgnoreCase(String firstname);

    Set<Employee> findAllByLastnameContainsIgnoreCase(String lastname);

    Set<Employee> findAllByPatronimycContainsIgnoreCase(String patronimyc);

    Set<Employee> findAllByPostalAddressContainsIgnoreCase(String postalAddress);

    Set<Employee> findAllByPersonalNumber(String personalNumber);

    Set<Employee> findAllByPhone(String phone);

    Set<Employee> findAllByEmail(String email);
}
