package kg.mega.projectemployeehandbook.models.dto.employee;

import kg.mega.projectemployeehandbook.configuration.PatternConfiguration;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

/**
 * DTO для создания профиля сотрудника
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class CreateEmployeeDTO {

    /* Имя, фамилия */
    @Pattern(
        regexp  = PatternConfiguration.EMPLOYEE_NAME_PATTERN,
        message = ErrorDescription.NAME_PATTERN
    ) String
        firstname,
        lastname;

    /* Отчество (опционально) */
    @Pattern(
        regexp  = PatternConfiguration.EMPLOYEE_OPTIONAL_NAME_PATTERN,
        message = ErrorDescription.NAME_PATTERN
    ) String patronimyc;

    /* Персональный номер */
    @NotBlank(message = ErrorDescription.PERSONAL_NUMBER_IS_EMPTY)
    String personalNumber;

    /* Номер телефона */
    @Pattern(
        regexp  = PatternConfiguration.PHONE_PATTERN,
        message = ErrorDescription.PHONE_PATTERN
    ) String phone;

    /* Электронная почта */
    @Pattern(
        regexp  = PatternConfiguration.EMAIL_PATTERN,
        message = ErrorDescription.EMAIL_PATTERN
    ) String email;

    /* Почтовый адрес */
    @Pattern(
        regexp  = PatternConfiguration.ADDRESS_PATTERN,
        message = ErrorDescription.ADDRESS_PATTERN
    ) String postalAddress;

    /* Статус и семейный статус */
    String
        familyStatus,
        status;

    /* ID позиции и структуры */
    Long
        positionId,
        structureId;

    /* Обязательные даты */
    @Pattern(
        regexp  = PatternConfiguration.DATE_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String
        birthDate,
        employmentDate;

    /* Опциональные даты.
     * При неуказании стартовых и конечных дат - будет присвоена текущая дата, а конечные даты останутся null
     * При указании конечных дат, добавление начальных - обязательно.
     */
    @Pattern(
        regexp  = PatternConfiguration.DATE_OPTIONAL_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String
        dismissalDate,
        structureStartDate,
        structureEndDate,
        positionStartDate,
        positionEndDate,
        statusStartDate,
        statusEndDate;
}