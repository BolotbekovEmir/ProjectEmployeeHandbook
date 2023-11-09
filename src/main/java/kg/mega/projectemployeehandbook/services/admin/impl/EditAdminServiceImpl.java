package kg.mega.projectemployeehandbook.services.admin.impl;

import kg.mega.projectemployeehandbook.errors.EditEntityException;
import kg.mega.projectemployeehandbook.errors.messages.ErrorDescription;
import kg.mega.projectemployeehandbook.errors.messages.InfoDescription;
import kg.mega.projectemployeehandbook.models.dto.admin.EditAdminDTO;
import kg.mega.projectemployeehandbook.models.entities.Admin;
import kg.mega.projectemployeehandbook.models.responses.RestResponse;
import kg.mega.projectemployeehandbook.repositories.AdminRepository;
import kg.mega.projectemployeehandbook.services.admin.EditAdminService;
import kg.mega.projectemployeehandbook.services.log.LoggingService;
import kg.mega.projectemployeehandbook.services.validation.ValidationUniqueService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.String.*;
import static kg.mega.projectemployeehandbook.models.enums.AdminRole.*;
import static lombok.AccessLevel.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class EditAdminServiceImpl implements EditAdminService {
    final ValidationUniqueService validationUniqueService;
    final AdminRepository         adminRepository;
    final LoggingService          loggingService;

    final RestResponse<EditEntityException> response = new RestResponse<>();

    @Override
    @Transactional
    public RestResponse<EditEntityException> editAdmin(EditAdminDTO editAdminDTO) {
        this.response.setErrorDescriptions(new ArrayList<>());

        String
            searchedAdminName = editAdminDTO.getSearchedAdminName(),
            newAdminName      = editAdminDTO.getNewAdminName(),
            newPersonalNumber = editAdminDTO.getNewPersonalNumber(),
            newPassword       = editAdminDTO.getNewPassword(),
            confirmPassword   = editAdminDTO.getConfirmNewPassword();

        boolean
            disableAdmin = editAdminDTO.isDisableAdmin(),
            enableAdmin  = editAdminDTO.isEnableAdmin();

        Admin admin = getAdminFindByName(searchedAdminName);

        updateAdminName(admin, newAdminName);
        updateAdminPersonalNumber(admin, newPersonalNumber);
        updateAdminPassword(admin, newPassword, confirmPassword);
        disableAdmin(admin, disableAdmin);
        enableAdmin(admin, enableAdmin);

        adminRepository.save(admin);

        String successMessage = format(InfoDescription.EDIT_ADMIN_FORMAT, searchedAdminName);
        loggingService.logInfo(successMessage);
        this.response.setHttpResponse(OK, OK.value());

        return this.response;
    }

    private Admin getAdminFindByName(String adminName) {
        Optional<Admin> optionalAdmin = adminRepository.findByAdminName(adminName);

        if (optionalAdmin.isEmpty()) {
            setErrorResponse(ErrorDescription.ADMIN_NOT_FOUND);
        }

        return optionalAdmin.orElseThrow(EditEntityException::new);
    }

    private void updateAdminName(Admin admin, String newAdminName) {
        if (!newAdminName.isBlank()) {
            if (validationUniqueService.isUniqueAdminName(newAdminName)) {
                admin.setAdminName(newAdminName);
            } else {
                setErrorResponse(ErrorDescription.ADMIN_NAME_UNIQUE);
            }
        }
    }

    private void updateAdminPersonalNumber(Admin admin, String personalNumber) {
        if (!personalNumber.isBlank()) {
            if (validationUniqueService.isUniqueAdminPersonalNumber(personalNumber)) {
                admin.setPersonalNumber(personalNumber);
            } else {
                if (!personalNumber.equals(admin.getPersonalNumber())) {
                    setErrorResponse(ErrorDescription.PERSONAL_NUMBER_UNIQUE);
                }
            }
        }
    }

    private void updateAdminPassword(Admin admin, String password, String confirmPassword) {
        if (!password.isBlank()) {
            if (password.equals(confirmPassword)) {
                // TODO: 07.11.2023 encoder
                admin.setPassword(password);
            } else {
                setErrorResponse(ErrorDescription.PASSWORDS_EQUAL);
            }
        }
    }

    private void disableAdmin(Admin admin, boolean disableAdmin) {
        if (disableAdmin) {
            if (!admin.getAdminRole().equals(DISABLE)) {
                admin.setAdminRole(DISABLE);
            } else {
                setErrorResponse(ErrorDescription.ADMIN_DISABLE);
            }
        }
    }

    private void enableAdmin(Admin admin, boolean enableAdmin) {
        if (enableAdmin) {
            if (!admin.getAdminRole().equals(ADMIN)) {
                admin.setAdminRole(ADMIN);
            } else {
                setErrorResponse(ErrorDescription.ADMIN_ENABLE);
            }
        }
    }

    private void setErrorResponse(String message) {
        this.response.setHttpResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value());
        this.response.addErrorDescription(message);
        loggingService.logError(message);
        throw new EditEntityException(this.response.getErrorDescriptions().toString());
    }
}