package kg.mega.projectemployeehandbook.models.dto.position;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class EditPositionDTO {

    Long
        editedPositionId,
        newMasterId;

    String newPositionName;

    Boolean
        disable,
        enable;

}
