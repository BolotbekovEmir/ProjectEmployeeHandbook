package kg.mega.projectemployeehandbook.errors;

import lombok.experimental.FieldDefaults;

import java.util.Collection;

import static lombok.AccessLevel.*;

@FieldDefaults(level = PRIVATE)
public class CreateEntityException extends RuntimeException {
    final Collection<String> errorDescriptions;

    public CreateEntityException(final Collection<String> errorDescriptions) {
        this.errorDescriptions = errorDescriptions;
    }

    public Collection<String> getErrorDescriptions() {
        return errorDescriptions;
    }
}
