package kg.mega.projectemployeehandbook.errors.handlers;

import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.ResponseEntity.*;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GlobalExceptionHandler {
    final LoggingService loggingService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        Map<String, String> errors = new HashMap<>();
        fieldErrors.forEach(f -> errors.put(f.getField(), f.getDefaultMessage()));

        loggingService.logError(errors.toString());
        return status(BAD_REQUEST).body(errors);
    }

}