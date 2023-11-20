package kg.mega.projectemployeehandbook.models.dto.position;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * DTO для создания позиции
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class CreatePositionDTO {
    /* Название позиции */
    String positionName;

    /* ID начальника-позиции */
    Long masterId;

    /* Активность позиции */
    Boolean active;

}
