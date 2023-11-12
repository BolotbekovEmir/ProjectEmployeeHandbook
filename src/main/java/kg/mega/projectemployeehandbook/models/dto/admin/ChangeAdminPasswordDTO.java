package kg.mega.projectemployeehandbook.models.dto.admin;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

import static kg.mega.projectemployeehandbook.configuration.PatternConfiguration.*;
import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class ChangeAdminPasswordDTO {

    @Pattern(
        regexp = PASSWORD_PATTERN,
        message = ErrorDescription.PASSWORD_PATTERN
    )
    String newAdminPassword;

    String confirmNewAdminPassword;

}