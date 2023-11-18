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
public class EditEmployeeStructureDTO {

    @NotBlank(message = ErrorDescription.PERSONAL_NUMBER_IS_EMPTY)
    String personalNumber;

    Long structureId;

    @Pattern(
        regexp  = PatternConfiguration.DATE_OPTIONAL_PATTERN,
        message = ErrorDescription.DATE_PATTERN
    ) String
        startDate,
        endDate;

    boolean
        isAddedOperation,
        isRemoveOperation;

}
