package kg.mega.projectemployeehandbook.models.dto.admin;

import kg.mega.projectemployeehandbook.configuration.PatternConfiguration;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class AuthAdminDTO {

    @Pattern(
        regexp = PatternConfiguration.ADMIN_NAME_PATTERN,
        message = ErrorDescription.ADMIN_AUTH_INVALID
    )
    String adminName;

    @Pattern(
        regexp = PatternConfiguration.PASSWORD_PATTERN,
        message = ErrorDescription.ADMIN_AUTH_INVALID
    )
    String password;

}
