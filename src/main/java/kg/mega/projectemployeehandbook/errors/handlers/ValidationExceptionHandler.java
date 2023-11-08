package kg.mega.projectemployeehandbook.errors.handlers;

import kg.mega.projectemployeehandbook.errors.CreateAdminException;
import kg.mega.projectemployeehandbook.errors.EditAdminException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(CreateAdminException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleCreateAdminException(CreateAdminException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorDescription.ADMIN_CREATE_INVALID, e.getMessage());
        return errors;
    }

    @ExceptionHandler(EditAdminException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Map<String, String> handleEditAdminException(EditAdminException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put(ErrorDescription.ADMIN_EDIT_INVALID, e.getMessage());
        return errors;
    }

}