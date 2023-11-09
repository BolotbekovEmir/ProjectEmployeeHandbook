package kg.mega.projectemployeehandbook.errors;

public class CreateEntityException extends RuntimeException {

    public CreateEntityException() {
    }

    public CreateEntityException(String message) {
        super(message);
    }

}
