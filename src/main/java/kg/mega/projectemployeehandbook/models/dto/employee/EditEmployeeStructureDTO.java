package kg.mega.projectemployeehandbook.models.dto.employee;

import kg.mega.projectemployeehandbook.configuration.PatternConfiguration;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static lombok.AccessLevel.*;

/**
 * DTO для изменения структуры у сотрудника
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class EditEmployeeStructureDTO {
    /* Изменяемый сотрудник находится по персональному номеру */
    @NotBlank(message = ErrorDescription.PERSONAL_NUMBER_IS_EMPTY)
    String personalNumber;

    /* ID структуры для операций */
    Long structureId;

    /* Стартовая и конечная дата
     * При неуказании начальной даты - устанавливается текущая
     * При неуказании конечной даты - устанавливается значение null
     * При указании конечной даты - начальная обязательна
     */
    @Pattern(
        regexp  = PatternConfiguration.DATE_OPTIONAL_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String
        startDate,
        endDate;

    /* Флаги для выбора операции добавления/удаления выбранной структуры у сотрудника */
    Boolean
        isAddedOperation,
        isRemoveOperation;

}
