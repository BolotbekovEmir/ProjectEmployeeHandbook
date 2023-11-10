package kg.mega.projectemployeehandbook.errors.handlers;


import kg.mega.projectemployeehandbook.errors.CreateEntityException;
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
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CreateEntityExceptionHandler {
    final LoggingService loggingService;

    @ExceptionHandler(CreateEntityException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleCreateEntityException(CreateEntityException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorDescription.CREATE_ENTITY_ERROR, e.getMessage());
        loggingService.logError(errors.toString());
        return errors;
    }

}
