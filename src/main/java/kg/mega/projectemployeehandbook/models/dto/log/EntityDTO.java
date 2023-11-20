package kg.mega.projectemployeehandbook.models.dto.log;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

/**
 * DTO для получения представления изменяемой сущности
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class EntityDTO {
    /* ID сущности */
    Long id;
    /* Название сущности */
    String name;

    @Override
    public String toString() {
        return '(' +
            "id=" + id +
            ", name='" + name + '\'' +
            ')';
    }
}
