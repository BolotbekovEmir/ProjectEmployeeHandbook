package kg.mega.projectemployeehandbook.models.dto.admin;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

import static lombok.AccessLevel.*;

/**
 * DTO для передачи информации о токене администратора.
 */
@Data
@FieldDefaults(level = PRIVATE)
public class TokenAdminDTO {
    String
        /* Имя администратора */
        adminName,
        /* Токен */
        token;

    /* Дата активности токена */
    Date activeTill;
}
