package kg.mega.projectemployeehandbook.models.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
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
    List<String> errors = new ArrayList<>();

    /**
     * Список описаний операции или проблем, связанных с ней.
     */
    List<String> descriptions = new ArrayList<>();

    /**
     * Статический метод для создания объекта Builder для класса ApiResult.
     *
     * @param <R> Тип данных для поля "response".
     * @return Объект Builder для настройки ApiResult.
     */
    public static <R> Builder<R> builder() {
        return new Builder<>();
    }

    /**
     * Вложенный класс Builder для удобного создания объектов ApiResult.
     *
     * @param <R> Тип данных для поля "response".
     */
    @FieldDefaults(level = PRIVATE)
    public static class Builder<R> {
        final ApiResult<R> apiResult;

        /**
         * Конструктор Builder, инициализирует новый объект ApiResult для настройки.
         */
        public Builder() {
            this.apiResult = new ApiResult<>();
        }

        /**
         * Устанавливает HTTP-статус для создаваемого объекта ApiResult.
         *
         * @param httpStatus HTTP-статус операции.
         * @return Ссылка на текущий объект Builder для цепочки вызовов методов.
         */
        public Builder<R> httpStatus(HttpStatus httpStatus) {
            this.apiResult.setHttpStatus(httpStatus);
            return this;
        }

        /**
         * Устанавливает код состояния для создаваемого объекта ApiResult.
         *
         * @param statusCode Код состояния операции.
         * @return Ссылка на текущий объект Builder для цепочки вызовов методов.
         */
        public Builder<R> statusCode(int statusCode) {
            this.apiResult.setStatusCode(statusCode);
            return this;
        }

        /**
         * Устанавливает ответ операции API для создаваемого объекта ApiResult.
         *
         * @param response Ответ операции API.
         * @return Ссылка на текущий объект Builder для цепочки вызовов методов.
         */
        public Builder<R> response(R response) {
            this.apiResult.setResponse(response);
            return this;
        }

        /**
         * Устанавливает список ошибок для создаваемого объекта ApiResult.
         *
         * @param errors Список ошибок операции.
         * @return Ссылка на текущий объект Builder для цепочки вызовов методов.
         */
        public Builder<R> errors(List<String> errors) {
            this.apiResult.setErrors(errors);
            return this;
        }

        /**
         * Устанавливает список описаний для создаваемого объекта ApiResult.
         *
         * @param descriptions Список описаний операции.
         * @return Ссылка на текущий объект Builder для цепочки вызовов методов.
         */
        public Builder<R> descriptions(List<String> descriptions) {
            this.apiResult.setDescriptions(descriptions);
            return this;
        }

        /**
         * Собирает и возвращает окончательный объект ApiResult с установленными значениями.
         *
         * @return Готовый объект ApiResult.
         */
        public ApiResult<R> build() {
            return this.apiResult;
        }
    }
}
