package kg.mega.projectemployeehandbook.errors.handlers;


import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class CreateEntityExceptionHandler {

    @ExceptionHandler(CreateEntityException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleEditAdminException(CreateEntityException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorDescription.CREATE_ENTITY_ERROR, e.getMessage());
        return errors;
    }

}
