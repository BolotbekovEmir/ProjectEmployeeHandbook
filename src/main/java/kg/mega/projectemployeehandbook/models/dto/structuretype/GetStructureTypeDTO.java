package kg.mega.projectemployeehandbook.models.dto.structuretype;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO для получения представления о типе структуры
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class GetStructureTypeDTO {
    /* ID структуры */
    Long structureTypeId;

    /* Имя структуры */
    String structureTypeName;

    /* Активность структуры */
    Boolean structureTypeActive;

}
