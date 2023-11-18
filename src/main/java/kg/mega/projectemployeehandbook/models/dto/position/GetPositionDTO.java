package kg.mega.projectemployeehandbook.models.dto.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GetPositionDTO {

    Long
        positionId,
        masterId;

    String positionName;

    boolean active;

}
