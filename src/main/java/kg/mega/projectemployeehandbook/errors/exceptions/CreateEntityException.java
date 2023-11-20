package kg.mega.projectemployeehandbook.errors.exceptions;

import lombok.experimental.FieldDefaults;

import java.util.Collection;

import static lombok.AccessLevel.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class CreateEntityException extends RuntimeException {
    Collection<String> errorDescriptions;

    public CreateEntityException(final Collection<String> errorDescriptions) {
        this.errorDescriptions = errorDescriptions;
    }

    public Collection<String> getErrorDescriptions() {
        return errorDescriptions;
    }
}