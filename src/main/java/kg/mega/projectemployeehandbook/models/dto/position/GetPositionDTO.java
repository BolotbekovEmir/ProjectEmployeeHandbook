package kg.mega.projectemployeehandbook.models.dto.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GetPositionDTO {

    Long
        positionId,
        masterId;

    String positionName;

    boolean active;

}
