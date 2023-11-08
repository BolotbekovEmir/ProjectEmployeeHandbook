package kg.mega.projectemployeehandbook.services.validation;

public interface ValidationUniqueService {

    boolean isUniqueAdminName(String adminName);

    boolean isUniqueAdminPersonalNumber(String personalNumber);

}
