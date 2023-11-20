package kg.mega.projectemployeehandbook.models.dto.log;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
public class ChangerDTO {
    Long id;
    String name;

    @Override
    public String toString() {
        return '(' +
            "id=" + id +
            ", name='" + name + '\'' +
            ')';
    }
}
