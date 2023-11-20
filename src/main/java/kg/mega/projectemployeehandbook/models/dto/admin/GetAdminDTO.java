package kg.mega.projectemployeehandbook.models.dto.admin;

import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

/**
 * DTO для получение информации об администраторе
 * */
@Data
@FieldDefaults(level = PRIVATE)
public class GetAdminDTO {

    String
        /* Имя администратора */
        adminName,
        /* Персональный номер */
        personalNumber;

    /* Роль администратора */
    AdminRole adminRole;

}
