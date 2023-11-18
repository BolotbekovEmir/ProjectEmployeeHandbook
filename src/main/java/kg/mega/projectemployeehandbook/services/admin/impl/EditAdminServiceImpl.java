package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.models.enums.ExceptionType;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.EditAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.enums.AdminRole;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.errors.ErrorCollectorService;
import kg.mega.projectemployeehandbook.services.admin.EditAdminService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.List.of;
import static kg.mega.projectemployeehandbook.models.enums.AdminRole.ADMIN;
import static kg.mega.projectemployeehandbook.models.enums.AdminRole.DISABLE;
import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class EditAdminServiceImpl implements EditAdminService {
    ValidationUniqueService validationUniqueService;
    ErrorCollectorService   errorCollectorService;
    AdminRepository         adminRepository;
    LoggingService          loggingService;

    @Override
    @Transactional
    public String editAdmin(EditAdminDTO editAdminDTO) {
        errorCollectorService.cleanup();

        String searchedAdminName = editAdminDTO.getSearchedAdminName();
        Admin admin = getAdminFindByName(searchedAdminName);

        if (!validateEditAdmin(editAdminDTO, admin)) {
            errorCollectorService.callException(ExceptionType.EDIT_ENTITY_EXCEPTION);
        }

        adminRepository.save(Objects.requireNonNull(admin));

        String operationSuccessMessage = format(InfoDescription.EDIT_ADMIN_FORMAT, searchedAdminName);
        loggingService.logInfo(operationSuccessMessage);
        return operationSuccessMessage;
    }

    private Admin getAdminFindByName(String adminName) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminName(adminName);
        if (optionalAdmin.isEmpty()) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.ADMIN_NAME_NOT_FOUND)
            );
        }
        return optionalAdmin.orElseThrow();
    }

    private boolean validateEditAdmin(EditAdminDTO editAdminDTO, Admin admin) {
        boolean valid = true;
        valid &= checkAndSetNewAdminName(editAdminDTO.getNewAdminName(), admin);
        valid &= checkAndSetNewPersonalNumber(editAdminDTO.getNewPersonalNumber(), admin);
        valid &= checkAndSetNewPassword(editAdminDTO.getNewPassword(), editAdminDTO.getConfirmNewPassword(), admin);
        valid &= checkAndSetAdminRole(editAdminDTO.isDisableAdmin(), editAdminDTO.isEnableAdmin(), admin.getAdminRole(), admin);
        return valid;
    }

    private boolean checkAndSetNewAdminName(String newAdminName, Admin admin) {
        if (newAdminName.isBlank()) {
            return true;
        }
        if (admin.getAdminName().equals(newAdminName)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.THIS_ADMIN_NAME_ALREADY_USED)
            );
            return false;
        }
        if (!validationUniqueService.isUniqueAdminName(newAdminName)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.ADMIN_NAME_UNIQUE)
            );
            return false;
        }
        admin.setAdminName(newAdminName);
        return true;
    }

    private boolean checkAndSetNewPersonalNumber(String newPersonalNumber, Admin admin) {
        if (newPersonalNumber.isBlank()) {
            return true;
        }
        if (admin.getPersonalNumber().equals(newPersonalNumber)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.THIS_ADMIN_PERSONAL_NUMBER_ALREADY_USED)
            );
            return false;
        }
        if (!validationUniqueService.isUniqueAdminPersonalNumber(newPersonalNumber)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.PERSONAL_NUMBER_UNIQUE)
            );
            return false;
        }
        admin.setPersonalNumber(newPersonalNumber);
        return true;
    }

    private boolean checkAndSetNewPassword(String newPassword, String confirmNewPassword, Admin admin) {
        if (newPassword.isBlank()) {
            return true;
        }
        if (admin.getPassword().equals(newPassword)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.THIS_PASSWORD_ALREADY_USED)
            );
            return false;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.PASSWORDS_EQUAL)
            );
            return false;
        }
        // TODO: 11.11.2023 Encoder
        admin.setPassword(newPassword);
        return true;
    }

    private boolean checkAndSetAdminRole(boolean disableAdmin, boolean enableAdmin, AdminRole currentRole, Admin admin) {
        if (disableAdmin && currentRole.equals(DISABLE)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.ADMIN_DISABLE)
            );
            return false;
        } else if (disableAdmin) {
            admin.setAdminRole(DISABLE);
        }
        if (enableAdmin && currentRole.equals(ADMIN)) {
            errorCollectorService.addErrorMessages(
                of(ErrorDescription.ADMIN_ENABLE)
            );
            return false;
        } else if (enableAdmin) {
            admin.setAdminRole(ADMIN);
        }
        return true;
    }
}