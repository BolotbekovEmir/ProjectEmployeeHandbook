package kg.mega.projectemployeehandbook.models.dto.structure;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class EditStructureDTO {

    Long
        structureId,
        newMasterId,
        newStructureTypeId;

    String newStructureName;

    boolean
        disable,
        enable;

}
