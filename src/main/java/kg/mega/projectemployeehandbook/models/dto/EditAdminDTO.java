package kg.mega.projectemployeehandbook.models.dto;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

import static kg.mega.projectemployeehandbook.configuration.PatternConfiguration.*;
import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class EditAdminDTO {

    @Pattern(regexp = EDIT_ADMIN_NAME_PATTERN, message = ErrorDescription.ADMIN_NAME_PATTERN)
    String searchedAdminName;

    @Pattern(regexp = EDIT_ADMIN_NAME_PATTERN, message = ErrorDescription.ADMIN_NAME_PATTERN)
    String newAdminName;

    String newPersonalNumber;

    @Pattern(regexp = EDIT_PASSWORD_PATTERN, message = ErrorDescription.PASSWORD_PATTERN)
    String newPassword;

    String confirmNewPassword;

    boolean
        disableAdmin,
        enableAdmin;

}