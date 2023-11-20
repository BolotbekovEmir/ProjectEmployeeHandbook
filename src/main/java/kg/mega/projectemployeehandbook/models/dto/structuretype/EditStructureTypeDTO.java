package kg.mega.projectemployeehandbook.models.dto.structuretype;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO для изменения тип структур
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class EditStructureTypeDTO {
    /* ID искомой редактируемой структуры */
    Long structureTypeId;

    /* Новое имя типа структуры (опционально) */
    String newStructureTypeName;

    /* Флаги указывающие на включение/выключение типа структуры (опционально) */
    Boolean
        disable,
        enable;
}
