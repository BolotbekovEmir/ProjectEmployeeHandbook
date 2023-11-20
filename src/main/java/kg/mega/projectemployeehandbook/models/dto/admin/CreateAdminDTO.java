package kg.mega.projectemployeehandbook.models.dto.admin;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static kg.mega.projectemployeehandbook.configuration.PatternConfiguration.*;
import static lombok.AccessLevel.*;

/**
 * DTO для создание профиля администратора
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class CreateAdminDTO {
    /* Имя администратора (уникально) */
    @Pattern(
        regexp  = ADMIN_NAME_PATTERN,
        message = ErrorDescription.ADMIN_NAME_PATTERN
    ) String adminName;

    /* Персональный номер администратор (уникально) */
    @NotBlank(message = ErrorDescription.PERSONAL_NUMBER_IS_EMPTY)
    String personalNumber;

    @Pattern(
        regexp  = PASSWORD_PATTERN,
        message = ErrorDescription.PASSWORD_PATTERN
    ) String
        /* Пароль */
        password,
        /* Подтверждение пароля */
        confirmPassword;
}
