package kg.mega.projectemployeehandbook.models.dto.position;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO для изменения позиции
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class EditPositionDTO {

    /* ID изменяемой позиции */
    Long editedPositionId;

    /* ID нового начальника-позииции (опционально) */
    Long newMasterId;

    /* Новое название позиции (опционально) */
    String newPositionName;

    /* Флаги указывающие на включение/выключение актуальности позиции (опционально) */
    Boolean
        disable,
        enable;

}
