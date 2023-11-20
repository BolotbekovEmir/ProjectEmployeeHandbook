package kg.mega.projectemployeehandbook.models.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

/**
 * Класс ApiResult представляет собой результат операции в API, включая HTTP-статус, код состояния,
 * ответ, список ошибок и описаний. Он обеспечивает более удобный способ создания объектов ApiResult
 * с использованием вложенного класса Builder.
 *
 * @param <R> Тип данных для поля "response" - это тип данных, который будет возвращен в ответ на API-запрос.
 */
@Getter
@Setter
@Builder
@FieldDefaults(level = PRIVATE)
public class ApiResult<R> {
    /**
     * HTTP-статус операции, например, HttpStatus.OK.
     */
    HttpStatus httpStatus;

    /**
     * Код состояния операции, например, 200 для успешной операции.
     */
    int statusCode;

    /**
     * Ответ операции API, который может быть любого типа (указанного как R).
     */
    R response;

    /**
     * Список ошибок, возникших в результате операции.
     */
    List<String> errors;

    /**
     * Список описаний операции или проблем, связанных с ней.
     */
    List<String> descriptions;
}
