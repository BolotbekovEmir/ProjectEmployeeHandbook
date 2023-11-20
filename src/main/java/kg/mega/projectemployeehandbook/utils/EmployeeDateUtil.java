package kg.mega.projectemployeehandbook.utils;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.ErrorCollector;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static lombok.AccessLevel.*;

/**
 * Утилитарный компонент для работы с датами сотрудников.
 * Предоставляет методы для парсинга дат из строк, валидации и форматирования дат.
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EmployeeDateUtil {
    ErrorCollector errorCollector;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Парсит строку даты в объект LocalDate или возвращает null, если строка пустая.
     * @param date Строка, содержащая дату.
     * @return LocalDate, представляющий дату из строки, или null, если строка пустая.
     */
    public LocalDate parseOrNull(String date) {
        return date.isBlank()
            ? null
            : LocalDate.parse(date, formatter);
    }

    /**
     * Парсит строку даты в объект LocalDate или возвращает текущую дату, если строка пустая.
     * @param date Строка, содержащая дату.
     * @return LocalDate, представляющий дату из строки, или текущую дату, если строка пустая.
     */
    public LocalDate parseOrNow(String date) {
        return date.isBlank()
            ? LocalDate.now()
            : LocalDate.parse(date, formatter);
    }

    /**
     * Проверяет пару дат - начальную и конечную, выдает ошибку, если конечная дата присутствует, а начальная - нет.
     * @param startDate Начальная дата в виде строки.
     * @param endDate Конечная дата в виде строки.
     */
    public void validateDatePair(String startDate, String endDate) {
        if (!endDate.isBlank() && startDate.isBlank()) {
            errorCollector.addErrorMessages(
                List.of(
                    String.format(ErrorDescription.END_DATE_IS_PRESENT_BUT_START_DATE_IS_NULL_FORMAT, endDate)
                )
            );
        }
    }

    /**
     * Возвращает объект форматтера даты.
     * @return DateTimeFormatter для форматирования дат.
     */
    public DateTimeFormatter getFormatter() {
        return formatter;
    }
}