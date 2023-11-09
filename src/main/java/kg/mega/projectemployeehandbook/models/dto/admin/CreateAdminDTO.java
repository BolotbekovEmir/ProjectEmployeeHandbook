package kg.mega.projectemployeehandbook.models.dto.admin;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

import static kg.mega.projectemployeehandbook.configuration.PatternConfiguration.*;
import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class CreateAdminDTO {

    @Pattern(regexp = ADMIN_NAME_PATTERN, message = ErrorDescription.ADMIN_NAME_PATTERN)
    String adminName;

    String personalNumber;

    @Pattern(regexp = PASSWORD_PATTERN, message = ErrorDescription.PASSWORD_PATTERN)
    String password;

    String confirmPassword;

}
