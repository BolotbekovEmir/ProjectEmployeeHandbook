package kg.mega.projectemployeehandbook.services.employee;

import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeePositionDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeProfileDTO;
import kg.mega.projectemployeehandbook.models.dto.employee.EditEmployeeStructureDTO;

public interface EditEmployeeService {
    String editEmployeeProfile(EditEmployeeProfileDTO editEmployeeProfileDTO);
    String editEmployeePosition(EditEmployeePositionDTO editEmployeePositionDTO);
    String editEmployeeStructure(EditEmployeeStructureDTO editEmployeeStructureDTO);
}
