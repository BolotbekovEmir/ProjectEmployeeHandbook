package kg.mega.projectemployeehandbook.models.dto.structure;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class EditStructureTypeDTO {

    long structureTypeId;

    String newStructureTypeName;

    boolean
        disable,
        enable;

}
