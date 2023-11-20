package kg.mega.projectemployeehandbook.models.dto.log;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

/**
 * DTO для получения представления об изменяемых полях
 * */
@Data
@Builder
@FieldDefaults(level = PRIVATE)
public class FieldUpdateDTO {
    /* Название изменяемого поля */
    String name;

    /* Информация о значениях поля {старое значение=новое значение} */
    Map<String, String> values;

    @Override
    public String toString() {
        return '(' +
            "name='" + name + '\'' +
            ", values=" + values +
            ')';
    }
}
