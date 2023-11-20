package kg.mega.projectemployeehandbook.models.dto.log;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * DTO для получения представления об инициаторе
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class ChangerDTO {
    /* ID инициатора */
    Long id;
    /* Имя инициатора */
    String name;

    @Override
    public String toString() {
        return '(' +
            "id=" + id +
            ", name='" + name + '\'' +
            ')';
    }
}
