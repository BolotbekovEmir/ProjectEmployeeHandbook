package kg.mega.projectemployeehandbook.models.dto.structure;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class CreateStructureDTO {

    String structureName;

    Long
        structureTypeId,
        masterId;

    Boolean active;

}
