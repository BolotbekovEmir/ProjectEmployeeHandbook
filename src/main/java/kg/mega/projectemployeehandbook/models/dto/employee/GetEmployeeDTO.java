package kg.mega.projectemployeehandbook.models.dto.employee;

import kg.mega.projectemployeehandbook.models.dto.position.GetPositionDTO;
import kg.mega.projectemployeehandbook.models.enums.FamilyStatus;
import kg.mega.projectemployeehandbook.models.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

import static lombok.AccessLevel.*;

/**
 * DTO для получения представления сотрудника
 * */
@Data
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GetEmployeeDTO {
    String
        /* Содержит ФИО/ФИ */
        fullName,
        /* Персональный номер */
        personalNumber,
        /* Номер телефона */
        phone,
        /* Электронная почта */
        email,
        /* Почтовый адрес */
        postalAddress,
        /* Путь до фотографии (глобальный) */
        pathPhoto;

    /* Семейный статус */
    FamilyStatus familyStatus;

    /* Нынешний статус */
    Status status;

    /* Набор позиций сотрудника */
    Set<GetPositionDTO> positions;

    /* Набор структур сотрудника */
    Set<String> structures;

    /* Даты трудоустройства и рождения */
    LocalDate
        employmentDate,
        birthDate;
}
