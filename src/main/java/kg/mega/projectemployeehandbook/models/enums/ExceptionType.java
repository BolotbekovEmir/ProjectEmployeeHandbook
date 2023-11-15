package kg.mega.projectemployeehandbook.models.enums;

import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.*;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public enum ExceptionType {
    CREATE_ENTITY_EXCEPTION("CreateEntityException"),
    EDIT_ENTITY_EXCEPTION("EditEntityException"),
    GET_ENTITY_EXCEPTION("GetEntityException");

    String stringName;

    ExceptionType(String stringName) {
        this.stringName = stringName;
    }

    public String getStringName() {
        return stringName;
    }
}
