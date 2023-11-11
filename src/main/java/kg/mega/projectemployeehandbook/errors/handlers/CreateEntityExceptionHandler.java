package kg.mega.projectemployeehandbook.errors.handlers;


import kg.mega.projectemployeehandbook.errors.CreateEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.models.responses.ApiResult;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static java.lang.String.*;
import static java.util.List.*;
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
    public ResponseEntity<ApiResult> handleCreateEntityException(CreateEntityException exception) {
        String errorName = ErrorDescription.CREATE_ENTITY_ERROR;
        List<String> descriptions = exception.getErrorDescriptions().stream().toList();

        loggingService.logError(
            format("%s: %s", errorName, descriptions)
        );

        return ResponseEntity.badRequest().body(
            ApiResult.builder()
                .httpStatus(BAD_REQUEST)
                .statusCode(BAD_REQUEST.value())
                .response(ExceptionType.CREATE_ENTITY_EXCEPTION.getStringName())
                .errors(of(errorName))
                .descriptions(descriptions)
                .build()
        );
    }

}
