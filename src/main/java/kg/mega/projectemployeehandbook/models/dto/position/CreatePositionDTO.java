package kg.mega.projectemployeehandbook.models.dto.position;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class CreatePositionDTO {

    String positionName;

    Long masterId;

    Boolean active;

}
