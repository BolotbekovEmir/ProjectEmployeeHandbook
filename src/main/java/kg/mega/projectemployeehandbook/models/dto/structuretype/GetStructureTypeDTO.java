package kg.mega.projectemployeehandbook.models.dto.structuretype;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class GetStructureTypeDTO {

    Long structureTypeId;

    String structureTypeName;

    Boolean structureTypeActive;

}
