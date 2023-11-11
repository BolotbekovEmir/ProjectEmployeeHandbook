package kg.mega.projectemployeehandbook.errors;

import lombok.experimental.FieldDefaults;

import java.util.Collection;

import static lombok.AccessLevel.*;

@FieldDefaults(level = PRIVATE)
public class GetEntityException extends RuntimeException {
    final Collection<String> errorMessages;

    public GetEntityException(final Collection<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Collection<String> getErrorMessages() {
        return errorMessages;
    }
}
