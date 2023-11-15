package kg.mega.projectemployeehandbook.services.validation;

public interface ValidationUniqueService {

    boolean isUniqueAdminName(String adminName);

    boolean isUniqueAdminPersonalNumber(String personalNumber);

    boolean isUniqueEmployeePersonalNumber(String personalNumber);

    boolean isUniquePhone(String phone);

    boolean isUniqueEmail(String email);

}
