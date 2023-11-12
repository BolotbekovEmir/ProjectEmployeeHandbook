package kg.mega.projectemployeehandbook.errors;

import lombok.experimental.FieldDefaults;

import java.util.Collection;

import static lombok.AccessLevel.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditEntityException extends RuntimeException {
    Collection<String> errorMessages;

    public EditEntityException(final Collection<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public Collection<String> getErrorMessages() {
        return errorMessages;
    }
}
