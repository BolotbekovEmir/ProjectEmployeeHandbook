package kg.mega.projectemployeehandbook.errors.handlers;

import kg.mega.projectemployeehandbook.errors.GetEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class GetEntityExceptionHandler {
    final LoggingService loggingService;

    @ExceptionHandler(GetEntityException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public Map<String, String> handleGetEntityException(GetEntityException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorDescription.ENTITY_NOT_FOUND, e.getMessage());
        loggingService.logError(errors.toString());
        return errors;
    }

}
