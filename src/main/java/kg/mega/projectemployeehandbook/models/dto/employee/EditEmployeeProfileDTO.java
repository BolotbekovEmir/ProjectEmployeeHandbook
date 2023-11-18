package kg.mega.projectemployeehandbook.models.dto.employee;

import kg.mega.projectemployeehandbook.configuration.PatternConfiguration;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class    EditEmployeeProfileDTO {

    /* Изменение профиля сотрудника по персональному номеру */
    @NotBlank(message = ErrorDescription.PERSONAL_NUMBER_IS_EMPTY)
    String personalNumber;

    @Pattern(
        regexp  = PatternConfiguration.EMPLOYEE_OPTIONAL_NAME_PATTERN,
        message = ErrorDescription.NAME_PATTERN
    ) String
        newFirstname,
        newLastname,
        newPatronimyc;

    String newPersonalNumber;

    @Pattern(
        regexp  = PatternConfiguration.PHONE_OPTIONAL_PATTERN,
        message = ErrorDescription.PHONE_PATTERN
    ) String newPhone;

    @Pattern(
        regexp  = PatternConfiguration.EMAIL_OPTIONAL_PATTERN,
        message = ErrorDescription.EMAIL_PATTERN
    ) String newEmail;

    @Pattern(
        regexp  = PatternConfiguration.ADDRESS_OPTIONAL_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String newPostalAddress;

    String newPathPhoto;

    String
        newFamilyStatus,
        newStatus;

    @Pattern(
        regexp  = PatternConfiguration.DATE_OPTIONAL_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String
        newBirthDate,
        newEmploymentDate,
        newDismissalDate;

}
