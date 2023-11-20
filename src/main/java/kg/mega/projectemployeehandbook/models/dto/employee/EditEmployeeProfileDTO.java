package kg.mega.projectemployeehandbook.models.dto.employee;

import kg.mega.projectemployeehandbook.configuration.PatternConfiguration;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

/**
 * DTO для изменения профиля сотрудника
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class EditEmployeeProfileDTO {
    /* Изменение профиля сотрудника по персональному номеру */
    @NotBlank(message = ErrorDescription.PERSONAL_NUMBER_IS_EMPTY)
    String personalNumber;

    /* Новое ФИО (опционально) */
    @Pattern(
        regexp  = PatternConfiguration.EMPLOYEE_OPTIONAL_NAME_PATTERN,
        message = ErrorDescription.NAME_PATTERN
    ) String
        newFirstname,
        newLastname,
        newPatronimyc;

    /* Новый персональный номер (опционально) */
    String newPersonalNumber;

    /* Новый номер телефона (опционально) */
    @Pattern(
        regexp  = PatternConfiguration.PHONE_OPTIONAL_PATTERN,
        message = ErrorDescription.PHONE_PATTERN
    ) String newPhone;

    /* Новая электронная почта (опционально) */
    @Pattern(
        regexp  = PatternConfiguration.EMAIL_OPTIONAL_PATTERN,
        message = ErrorDescription.EMAIL_PATTERN
    ) String newEmail;

    /* Новый почтовый адрес (опционально) */
    @Pattern(
        regexp  = PatternConfiguration.ADDRESS_OPTIONAL_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String newPostalAddress;

    /* Новые названия статусов (опционально) */
    String
        newFamilyStatus,
        newStatus;

    /* Новые даты (опционально)
     * Чтобы уволить сотрудника - установить дату увольнения
     */
    @Pattern(
        regexp  = PatternConfiguration.DATE_OPTIONAL_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String
        newBirthDate,
        newEmploymentDate,
        newDismissalDate;
}
