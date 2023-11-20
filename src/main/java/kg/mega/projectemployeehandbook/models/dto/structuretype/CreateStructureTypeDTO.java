package kg.mega.projectemployeehandbook.models.dto.structuretype;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO Для создания тип стуктур
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class CreateStructureTypeDTO {
    /* Название типа структуры */
    String structureTypeName;

    /* Активность */
    Boolean active;

}
