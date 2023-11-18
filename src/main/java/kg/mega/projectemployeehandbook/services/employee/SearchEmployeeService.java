package kg.mega.projectemployeehandbook.services.employee;

import kg.mega.projectemployeehandbook.models.dto.employee.GetEmployeeDTO;

import java.util.Set;

public interface SearchEmployeeService {
    Set<GetEmployeeDTO> searchEmployees(String searchField);
}
