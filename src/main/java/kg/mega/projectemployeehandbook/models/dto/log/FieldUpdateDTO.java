package kg.mega.projectemployeehandbook.models.dto.log;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(level = PRIVATE)
public class FieldUpdateDTO {
    String name;
    Map<String, String> values;

    @Override
    public String toString() {
        return '(' +
            "name='" + name + '\'' +
            ", values=" + values +
            ')';
    }
}
