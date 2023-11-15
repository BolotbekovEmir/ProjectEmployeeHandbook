package kg.mega.projectemployeehandbook.errors.exceptions;

import lombok.experimental.FieldDefaults;

import java.util.Collection;

import static lombok.AccessLevel.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class GetEntityException extends RuntimeException {
    Collection<String> errorMessages;

    public GetEntityException(final Collection<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Collection<String> getErrorMessages() {
        return errorMessages;
    }
}
