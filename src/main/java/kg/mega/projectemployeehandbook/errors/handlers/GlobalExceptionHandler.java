package kg.mega.projectemployeehandbook.errors.handlers;

import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

import static java.lang.String.*;
import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

/**
 * Глобальный обработчик исключений для обработки ошибок валидации методов.
 */
@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {
    LoggingService loggingService;

    /**
     * Обрабатывает исключение MethodArgumentNotValidException и формирует ответ с ошибкой валидации.
     *
     * @param exception исключение, связанное с ошибкой валидации методов
     * @return ResponseEntity с результатом операции и описанием ошибки валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiResult> handleValidationException(MethodArgumentNotValidException exception) {
        // Получение информации о невалидных полях
        BindingResult bindingResult  = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // Два листа для записи невалидных полей и описанием ошибки
        List<String>
            fields   = new ArrayList<>(),
            messages = new ArrayList<>();

        // Добавление сообщений в листы fields, messages
        fieldErrors.forEach(fieldError -> {
            fields.add(format("%s - недопустимый формат", fieldError.getField()));
            messages.add(fieldError.getDefaultMessage());
        });

        // Логирует ошибку валидации
        loggingService.logError(
            format("%s: %s", fields, messages)
        );

        // Формирует ответ с ошибкой валидации
        return ResponseEntity.badRequest().body(
            ApiResult.builder()
                .httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .response(
                    "MethodArgumentNotValidException"
                ).errors(fields)
                .descriptions(messages)
                .build()
        );
    }
}