package kg.mega.projectemployeehandbook.models.dto.position;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO для получения представления о позиции
 * */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GetPositionDTO {
    /* ID позиции */
    Long positionId;

    /* ID начальника-позиции */
    Long masterId;

    /* Имя позиции */
    String positionName;

    /* Активность позиции */
    Boolean active;

}
