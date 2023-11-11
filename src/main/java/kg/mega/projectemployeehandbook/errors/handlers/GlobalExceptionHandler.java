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

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GlobalExceptionHandler {
    final LoggingService loggingService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ApiResult> handleValidationException(MethodArgumentNotValidException exception) {
        BindingResult bindingResult  = exception.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        List<String>
            fields   = new ArrayList<>(),
            messages = new ArrayList<>();

        fieldErrors.forEach(fieldError -> {
            fields.add(format("%s - недопустимый формат", fieldError.getField()));
            messages.add(fieldError.getDefaultMessage());
        });

        loggingService.logError(
            format("%s: %s", fields, messages)
        );

        return ResponseEntity.badRequest().body(
            ApiResult.builder()
                .httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .response("MethodArgumentNotValidException")
                .errors(fields)
                .descriptions(messages)
                .build()
        );
    }

}