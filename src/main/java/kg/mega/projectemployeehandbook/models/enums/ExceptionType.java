package kg.mega.projectemployeehandbook.models.enums;

public enum ExceptionType {
    CREATE_ENTITY_EXCEPTION("CreateEntityException"),
    EDIT_ENTITY_EXCEPTION("EditEntityException"),
    GET_ENTITY_EXCEPTION("GetEntityException");

    private final String stringName;

    ExceptionType(String stringName) {
        this.stringName = stringName;
    }

    public String getStringName() {
        return stringName;
    }
}
