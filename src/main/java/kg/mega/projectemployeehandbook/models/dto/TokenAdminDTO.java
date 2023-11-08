package kg.mega.projectemployeehandbook.models.dto;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

import static lombok.AccessLevel.*;

@Data
@FieldDefaults(level = PRIVATE)
public class TokenAdminDTO {

    String
        adminName,
        token;

    Date activeTill;

}
