package kg.mega.projectemployeehandbook.models.dto.admin;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Pattern;

import static kg.mega.projectemployeehandbook.configuration.PatternConfiguration.*;
import static lombok.AccessLevel.*;

/**
 * DTO для изменение профиля администратора
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class EditAdminDTO {

    /* Изменяемый администратор находится по имени */
    @Pattern(
        regexp  = EDIT_ADMIN_NAME_PATTERN,
        message = ErrorDescription.ADMIN_NAME_PATTERN
    ) String searchedAdminName;

    /* Новое имя администратора (опционально) */
    @Pattern(
        regexp  = EDIT_ADMIN_NAME_PATTERN,
        message = ErrorDescription.ADMIN_NAME_PATTERN
    ) String newAdminName;

    /* Новый персональный номер администратора (опционально) */
    String newPersonalNumber;

    /* Новый пароль администратора (опционально)
       При вводе нового пароля, его подтверждение обязательно */
    @Pattern(
        regexp  = PASSWORD_OPTIONAL_PATTERN,
        message = ErrorDescription.PASSWORD_PATTERN
    ) String
        newPassword,
        confirmNewPassword;

    /* Флаги для включения/выключения профиля администратора */
    Boolean
        disableAdmin,
        enableAdmin;

}
