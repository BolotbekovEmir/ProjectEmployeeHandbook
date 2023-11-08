package kg.mega.projectemployeehandbook.models.dto;

import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class GetAdminDTO {

    String
        adminName,
        personalNumber;

    AdminRole adminRole;

}
