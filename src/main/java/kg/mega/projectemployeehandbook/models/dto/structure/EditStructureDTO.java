package kg.mega.projectemployeehandbook.models.dto.structure;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO для изменения структуры
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class EditStructureDTO {
    Long
        /* ID искомой структуры для редактирования */
        structureId,
        /* ID новой структуры-начальника (опционально) */
        newMasterId,
        /* ID нового типа структуры (опционально) */
        newStructureTypeId;

    /* Новое имя структуры (опционально) */
    String newStructureName;

    /* Флаги указывающие на включение/выключение структуры (опционально) */
    Boolean
        disable,
        enable;
}
