package kg.mega.projectemployeehandbook.models.dto.structure;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO для создания структуры
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class CreateStructureDTO {
    /* Имя структуры */
    String structureName;

    /* ID типа стурктуры */
    Long structureTypeId;

    /* ID структуры-начальника */
    Long masterId;

    /* Активность структуры */
    Boolean active;
}
