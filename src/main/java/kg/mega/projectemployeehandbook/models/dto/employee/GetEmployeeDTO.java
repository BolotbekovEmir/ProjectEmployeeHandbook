package kg.mega.projectemployeehandbook.models.dto.employee;

import kg.mega.projectemployeehandbook.models.dto.position.GetPositionDTO;
import kg.mega.projectemployeehandbook.models.enums.FamilyStatus;
import kg.mega.projectemployeehandbook.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GetEmployeeDTO {

    String
        fullName,
        personalNumber,
        phone,
        email,
        postalAddress,
        pathPhoto;

    FamilyStatus familyStatus;

    Status status;

    Set<GetPositionDTO> positions;

    Set<String> structures;

    LocalDate
        employmentDate,
        birthDate;

}
