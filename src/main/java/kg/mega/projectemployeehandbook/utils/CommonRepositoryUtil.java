package kg.mega.projectemployeehandbook.utils;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.services.ErrorCollectorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.List.*;
import static lombok.AccessLevel.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class CommonRepositoryUtil {
    final ErrorCollectorService errorCollectorService;

    public <E> E getEntityById(
        Long entityId,
        CrudRepository<E, Long> repository,
        String errorMessage,
        ExceptionType exceptionType
    ) {
        if (entityId != null) {
            Optional<E> optionalEntity = repository.findById(entityId);

            if (optionalEntity.isEmpty()) {
                errorCollectorService.addErrorMessages(of(errorMessage));
                errorCollectorService.callException(exceptionType);
            }

            return optionalEntity.orElseThrow();
        }

        errorCollectorService.addErrorMessages(
            of(ErrorDescription.ENTITY_ID_IS_NULL)
        );
        errorCollectorService.callException(ExceptionType.GET_ENTITY_EXCEPTION);
        return null;
    }
}