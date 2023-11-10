package kg.mega.projectemployeehandbook.errors.handlers;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.AccessLevel;
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
public class EditEntityExceptionHandler {
    final LoggingService loggingService;

    @ExceptionHandler(EditEntityException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleEditEntityException(EditEntityException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorDescription.EDIT_ENTITY_ERROR, e.getMessage());
        loggingService.logError(errors.toString());
        return errors;
    }

}
